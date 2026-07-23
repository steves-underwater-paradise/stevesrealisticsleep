package io.github.steveplays28.stevesrealisticsleep.mixin;

import io.github.steveplays28.stevesrealisticsleep.api.StevesRealisticSleepApi;
import io.github.steveplays28.stevesrealisticsleep.extension.ServerWorldExtension;
import io.github.steveplays28.stevesrealisticsleep.mixin.accessor.AbstractCauldronBlockAccessor;
import io.github.steveplays28.stevesrealisticsleep.util.SleepMathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.SleepStatus;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.ticks.LevelTicks;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep.*;
import static io.github.steveplays28.stevesrealisticsleep.util.SleepMathUtil.*;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements ServerWorldExtension {
	protected ServerLevelMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, Supplier<ProfilerFiller> profiler,
			boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
		super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
	}

	@Shadow @Final protected Raids raids;

	@Shadow @Final List<ServerPlayer> players;

	@Shadow @Final private static int MAX_SCHEDULED_TICKS_PER_TICK;

	@Shadow @Final private ServerLevelData serverLevelData;

	@Shadow @Final private MinecraftServer server;

	@Shadow @Final private SleepStatus sleepStatus;

	@Shadow @Final private ServerChunkCache chunkSource;

	@Shadow @Final private LevelTicks<Fluid> fluidTicks;

	@Shadow @Final private boolean tickTime;

	@Shadow
	public abstract ServerLevel getLevel();

	@Shadow
	public abstract List<ServerPlayer> players();

	@Shadow
	public abstract void tickPrecipitation(BlockPos blockPos);

	@Shadow
	protected abstract void wakeUpAllPlayers();

	@Shadow
	protected abstract BlockPos findLightningTargetAround(BlockPos pos);

	@Shadow
	protected abstract void tickFluid(BlockPos pos, Fluid fluid);

	@Unique private double stevesrealisticsleep$timeStepPerTick = 2;
	@Unique private long stevesrealisticsleep$tickDelay;
	@Unique private MutableComponent stevesrealisticsleep$sleepMessage;
	@Unique private boolean stevesrealisticsleep$shouldSkipWeather = false;
	@Unique private int stevesrealisticsleep$consecutiveSleepTicks = 0;
	@Unique private int stevesrealisticsleep$ticksSinceLastTicksPerSecondCheck = 0;
	@Unique private long stevesrealisticsleep$previousTime = System.currentTimeMillis();
	@Unique private double stevesrealisticsleep$estimatedTicksPerSecond = 20.0;

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getInt(Lnet/minecraft/world/level/GameRules$Key;)I"))
	public void stevesrealisticsleep$tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		// Estimate ticks per second every 10 ticks
		if (stevesrealisticsleep$ticksSinceLastTicksPerSecondCheck >= 10) {
			long currentTime = System.currentTimeMillis();
			stevesrealisticsleep$estimatedTicksPerSecond = (double) stevesrealisticsleep$ticksSinceLastTicksPerSecondCheck / (currentTime - stevesrealisticsleep$previousTime) * 1000;
			stevesrealisticsleep$ticksSinceLastTicksPerSecondCheck = 0;
			stevesrealisticsleep$previousTime = currentTime;
		}
		stevesrealisticsleep$ticksSinceLastTicksPerSecondCheck += 1;

		// Calculate seconds until awake
		int sleepingPlayerCount = sleepStatus.amountSleeping();
		int playerCount = players().size();
		double sleepingRatio = (double) sleepingPlayerCount / playerCount;
		stevesrealisticsleep$timeStepPerTick = SleepMathUtil.calculateTimeStepPerTick(sleepingRatio, config.sleepSpeedMultiplier, stevesrealisticsleep$timeStepPerTick);
		int timeOfDay = StevesRealisticSleepApi.getTimeOfDay(this);
		int secondsUntilAwake = Math.abs(SleepMathUtil.calculateSecondsUntilAwake(timeOfDay, stevesrealisticsleep$timeStepPerTick, stevesrealisticsleep$estimatedTicksPerSecond));

		// Check if the night has (almost) ended and the weather should be skipped
		if (secondsUntilAwake <= 2 && stevesrealisticsleep$shouldSkipWeather) {
			stevesrealisticsleep$clearWeather();
			stevesrealisticsleep$shouldSkipWeather = false;
		}

		// Check if anyone is sleeping
		if (sleepingPlayerCount <= 0) {
			// Reset consecutive sleep ticks
			stevesrealisticsleep$consecutiveSleepTicks = 0;

			return;
		}

		// Fetch values and construct night, day, or thunderstorm text
		var isNight = SleepMathUtil.isNightTime(timeOfDay);
		var nightDayOrThunderstormText = Component.translatable(String.format("%s.text.%s", MOD_NAMESPACE, serverLevelData.isThundering() ? "thunderstorm" : isNight ? "night" : "day"));

		// Check if the required percentage of players are sleeping
		if (!StevesRealisticSleepApi.isSleeping(this)) {
			if (!config.sendNotEnoughPlayersSleepingMessage) {
				return;
			}

			double playersRequiredToSleepRatio = server.getGameRules().getInt(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE) / 100d;
			int playersRequiredToSleep = (int) Math.ceil(playersRequiredToSleepRatio * playerCount);
			for (ServerPlayer player : players) {
				player.sendSystemMessage(Component.translatable(String.format("%s.text.not_enough_players_sleeping_message", MOD_NAMESPACE), sleepingPlayerCount, playerCount, playersRequiredToSleep,
						playerCount, nightDayOrThunderstormText), true);
			}
			return;
		}

		// Do calculations
		int timeStepPerTickRounded = (int) Math.round(stevesrealisticsleep$timeStepPerTick);
		int ticksUntilAwake = SleepMathUtil.calculateTicksUntilAwake(timeOfDay);

		// Advance time
		boolean doDayLightCycle = server.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT);
		if (doDayLightCycle) {
			serverLevelData.setDayTime(serverLevelData.getDayTime() + timeStepPerTickRounded);
		}

		// Tick block entities
		int blockEntityTickSpeedMultiplier = (int) Math.round(config.blockEntityTickSpeedMultiplier);
		for (int i = blockEntityTickSpeedMultiplier; i > 1; i--) {
			tickBlockEntities();
		}

		// Tick chunks
		int chunkTickSpeedMultiplier = (int) Math.round(config.chunkTickSpeedMultiplier);
		for (int i = chunkTickSpeedMultiplier; i > 1; i--) {
			chunkSource.tick(shouldKeepTicking, true);
		}

		// Tick raid timers
		int raidTickSpeedMultiplier = (int) Math.round(config.raidTickSpeedMultiplier);
		for (int i = raidTickSpeedMultiplier; i > 1; i--) {
			raids.tick();
		}

		// Tick fluids
		int fluidScheduledTickSpeedMultiplier = (int) Math.round(config.fluidScheduledTickSpeedMultiplier);
		for (int i = fluidScheduledTickSpeedMultiplier; i > 1; i--) {
			fluidTicks.tick(serverLevelData.getGameTime(), MAX_SCHEDULED_TICKS_PER_TICK, this::tickFluid);
		}

		// Send new time to all players in the level
		server.getPlayerList().broadcastAll(new ClientboundSetTimePacket(serverLevelData.getGameTime(), serverLevelData.getDayTime(), doDayLightCycle), dimension());

		// Check if players are still supposed to be sleeping, and send a HUD message if so
		if (ticksUntilAwake > WAKE_UP_GRACE_PERIOD_TICKS) {
			if (stevesrealisticsleep$consecutiveSleepTicks >= MINIMUM_SLEEP_TICKS_TO_CLEAR_WEATHER) {
				stevesrealisticsleep$shouldSkipWeather = true;
			}

			if (config.sendSleepingMessage) {
				stevesrealisticsleep$sleepMessage = Component.translatable(String.format("%s.text.sleep_message", MOD_NAMESPACE), sleepingPlayerCount, playerCount).append(nightDayOrThunderstormText);
				if (isNight) {
					if (config.showTimeUntilDawn) {
						stevesrealisticsleep$sleepMessage.append(Component.translatable(String.format("%s.text.time_until_dawn", MOD_NAMESPACE), secondsUntilAwake));
					}
				} else if (config.showTimeUntilDusk) {
					stevesrealisticsleep$sleepMessage.append(Component.translatable(String.format("%s.text.time_until_dusk", MOD_NAMESPACE), secondsUntilAwake));
				}

				for (ServerPlayer player : players) {
					player.sendSystemMessage(stevesrealisticsleep$sleepMessage, true);
				}
			}

			stevesrealisticsleep$consecutiveSleepTicks += timeStepPerTickRounded;
		}

		if (ticksUntilAwake <= WAKE_UP_GRACE_PERIOD_TICKS) {
			// Wake up sleeping players
			this.wakeUpAllPlayers();

			// Reset time step per tick, to reset the exponential sleep speed curve calculation
			stevesrealisticsleep$timeStepPerTick = 2;
		}
	}

	@Inject(method = "tickTime", at = @At(value = "HEAD"), cancellable = true)
	public void stevesrealisticsleep$tickTimeWithTimeTickSpeedMultiplier(@NotNull CallbackInfo ci) {
		serverLevelData.getScheduledEvents().tick(this.server, this.getGameTime());

		if (!this.tickTime) {
			ci.cancel();
			return;
		}

		long l = this.getGameTime() + 1L;
		if (sleepStatus.amountSleeping() <= 0) {
			serverLevelData.setGameTime(l);
		}

		if (stevesrealisticsleep$tickDelay > 0L) {
			stevesrealisticsleep$tickDelay -= 1L;
			server.getPlayerList().broadcastAll(new ClientboundSetTimePacket(serverLevelData.getGameTime(), serverLevelData.getDayTime(), this.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)),
					dimension());

			ci.cancel();
			return;
		}

		if (sleepStatus.amountSleeping() > 0) {
			return;
		}

		if (this.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) {
			serverLevelData.setDayTime(this.getDayTime() + 1L);
		}

		stevesrealisticsleep$tickDelay = config.tickDelay;

		ci.cancel();
	}

	/**
	 * Cancels {@link ServerLevel#announceSleepStatus announceSleepStatus}.
	 *
	 * @author Steveplays28
	 * @reason Method's HUD messages conflict with Realistic Sleep's custom HUD messages.
	 */
	@SuppressWarnings("JavadocReference")
	@Inject(method = "announceSleepStatus", at = @At(value = "HEAD"), cancellable = true)
	private void stevesrealisticsleep$preventAnnouncingSleepStatus(@NotNull CallbackInfo ci) {
		ci.cancel();
	}

	@Inject(method = "tickChunk", at = @At(value = "HEAD"))
	private void stevesrealisticsleep$tickChunksWithChunkTickSpeedMultiplier(@NotNull LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
		if (!StevesRealisticSleepApi.isSleeping(this)) {
			return;
		}

		var profiler = this.getProfiler();
		var chunkPos = chunk.getPos();
		var chunkMinBlockX = chunkPos.getMinBlockX();
		var chunkMinBlockZ = chunkPos.getMinBlockZ();

		// Thunder tick speed multiplier
		profiler.push(String.format("Thunder (%s)", MOD_NAME));

		var thunderTickSpeedMultiplier = (int) Math.round(config.thunderTickSpeedMultiplier);
		for (int i = 0; i < thunderTickSpeedMultiplier; i++) {
			if (this.isRaining() && this.isThundering() && this.random.nextInt(100000) == 0) {
				var blockPos = this.findLightningTargetAround(this.getBlockRandomPos(chunkMinBlockX, 0, chunkMinBlockZ, 15));
				if (this.isRainingAt(blockPos)) {
					LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(this);
					if (lightningEntity != null) {
						lightningEntity.moveTo(Vec3.atBottomCenterOf(blockPos));
						lightningEntity.setVisualOnly(true);
						this.addFreshEntity(lightningEntity);
					}
				}
			}
		}

		if (randomTickSpeed <= 0) {
			profiler.pop();
			return;
		}

		// Ice and snow formation tick speed multiplier
		profiler.popPush(String.format("Form ice and snow (%s)", MOD_NAME));

		var iceAndSnowTickSpeedMultiplier = (int) Math.round(config.iceAndSnowTickSpeedMultiplier);
		for (int i = 0; i < iceAndSnowTickSpeedMultiplier; i++) {
			if (this.random.nextInt(16) == 0) {
				var blockPos = this.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, this.getBlockRandomPos(chunkMinBlockX, 0, chunkMinBlockZ, 15));
				BlockPos blockPosDown = blockPos.below();
				Biome biome = this.getBiome(blockPos).value();
				if (biome.shouldFreeze(this, blockPosDown, false)) {
					this.setBlock(blockPosDown, Blocks.ICE.defaultBlockState(), 3);
				}
				if (this.isRaining() && biome.shouldSnow(this, blockPos)) {
					this.setBlock(blockPos, Blocks.SNOW.defaultBlockState(), 3);
				}
			}
		}

		profiler.popPush(String.format("Tick blocks (%s)", MOD_NAME));
		for (int l = 0; l < chunk.getSections().length; l++) {
			var chunkSection = chunk.getSections()[l];
			if (!chunkSection.isRandomlyTicking()) {
				continue;
			}

			// Crop growth tick speed multiplier
			var cropGrowthTickSpeedMultiplier = (int) Math.round(config.cropGrowthTickSpeedMultiplier);
			for (int i = 0; i < cropGrowthTickSpeedMultiplier; i++) {
				int chunkSectionYOffset = SectionPos.sectionToBlockCoord(chunk.getSectionYFromSectionIndex(l));
				var randomPosInChunk = this.getBlockRandomPos(chunkMinBlockX, chunkSectionYOffset, chunkMinBlockZ, 15);
				var randomBlockStateInChunk =
						chunkSection.getBlockState(randomPosInChunk.getX() - chunkMinBlockX, randomPosInChunk.getY() - chunkSectionYOffset, randomPosInChunk.getZ() - chunkMinBlockZ);
				var randomBlockInChunk = randomBlockStateInChunk.getBlock();
				if (randomBlockInChunk instanceof CropBlock || randomBlockInChunk instanceof StemBlock || randomBlockInChunk instanceof CactusBlock || randomBlockInChunk instanceof SugarCaneBlock) {
					randomBlockStateInChunk.randomTick((ServerLevel) (Object) this, randomPosInChunk, random);
				}
			}

			// Precipitation tick speed multiplier
			var precipitationTickSpeedMultiplier = (int) Math.round(config.precipitationTickSpeedMultiplier);
			for (int i = 0; i < precipitationTickSpeedMultiplier; i++) {
				int chunkSectionYOffset = SectionPos.sectionToBlockCoord(chunk.getSectionYFromSectionIndex(l));
				var randomPosInChunk = this.getBlockRandomPos(chunkMinBlockX, chunkSectionYOffset, chunkMinBlockZ, 15);
				var biome = this.getBiome(randomPosInChunk).value();
				var precipitation = biome.getPrecipitationAt(randomPosInChunk);
				if (precipitation == Biome.Precipitation.NONE) {
					continue;
				}

				var randomBlockStateInChunk =
						chunkSection.getBlockState(randomPosInChunk.getX() - chunkMinBlockX, randomPosInChunk.getY() - chunkSectionYOffset, randomPosInChunk.getZ() - chunkMinBlockZ);
				var randomBlockInChunk = randomBlockStateInChunk.getBlock();
				if (randomBlockInChunk instanceof AbstractCauldronBlock cauldronBlock) {
					cauldronBlock.handlePrecipitation(randomBlockStateInChunk, this, randomPosInChunk, precipitation);
					((AbstractCauldronBlockAccessor) cauldronBlock).invokeTick(randomBlockStateInChunk, this.getLevel(), randomPosInChunk, random);
				}
			}

			// Random tick speed multiplier
			var blockRandomTickSpeedMultiplier = (int) Math.round(config.blockRandomTickSpeedMultiplier);
			var fluidRandomTickSpeedMultiplier = (int) Math.round(config.fluidRandomTickSpeedMultiplier);
			for (int j = 0; j < randomTickSpeed; j++) {
				int chunkSectionYOffset = SectionPos.sectionToBlockCoord(chunk.getSectionYFromSectionIndex(l));
				var randomPosInChunk = this.getBlockRandomPos(chunkMinBlockX, chunkSectionYOffset, chunkMinBlockZ, 15);
				var randomBlockStateInChunk =
						chunkSection.getBlockState(randomPosInChunk.getX() - chunkMinBlockX, randomPosInChunk.getY() - chunkSectionYOffset, randomPosInChunk.getZ() - chunkMinBlockZ);
				var fluidState = randomBlockStateInChunk.getFluidState();
				for (int k = 0; k < blockRandomTickSpeedMultiplier; k++) {
					if (randomBlockStateInChunk.isRandomlyTicking()) {
						randomBlockStateInChunk.randomTick(this.getLevel(), randomPosInChunk, this.random);
					}
				}
				for (int k = 0; k < fluidRandomTickSpeedMultiplier; k++) {
					if (fluidState.isRandomlyTicking()) {
						fluidState.randomTick(this, randomPosInChunk, this.random);
					}
				}
			}
		}

		profiler.pop();
	}

	@Unique
	private void stevesrealisticsleep$clearWeather() {
		boolean doWeatherCycle = server.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE);
		if (doWeatherCycle && (serverLevelData.isRaining() || serverLevelData.isThundering())) {
			// Reset weather clock and clear weather
			var nextRainTime = (int) (DAY_LENGTH * SleepMathUtil.getRandomNumberInRange(0.5, 7.5));
			serverLevelData.setRainTime(nextRainTime);
			serverLevelData.setThunderTime(nextRainTime + (Math.random() > 0 ? 1 : -1));

			serverLevelData.setThundering(false);
			serverLevelData.setRaining(false);
		}
	}
}
