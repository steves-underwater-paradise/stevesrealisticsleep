package com.github.steveplays28.realisticsleep.mixin;

import com.github.steveplays28.realisticsleep.SleepMath;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.SleepManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.raid.RaidManager;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ServerWorldProperties;
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

import static com.github.steveplays28.realisticsleep.RealisticSleep.MOD_ID;
import static com.github.steveplays28.realisticsleep.RealisticSleep.config;
import static com.github.steveplays28.realisticsleep.SleepMath.DAY_LENGTH;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
	@Unique
	public double nightTimeStepPerTick = 1;
	@Unique
	public int nightTimeStepPerTickRounded = 1;
	@Unique
	public long tickDelay;
	@Unique
	public MutableText sleepMessage;
	@Unique
	public Boolean shouldSkipWeather = false;

	@Shadow
	@Final
	protected RaidManager raidManager;
	@Shadow
	@Final
	List<ServerPlayerEntity> players;
	@Shadow
	@Final
	private ServerWorldProperties worldProperties;
	@Shadow
	@Final
	private MinecraftServer server;
	@Shadow
	@Final
	private SleepManager sleepManager;
	@Shadow
	@Final
	private ServerChunkManager chunkManager;
	@Shadow
	@Final
	private boolean shouldTickTime;

	@Shadow
	public abstract List<ServerPlayerEntity> getPlayers();

	@Shadow protected abstract void wakeSleepingPlayers();

	protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> registryEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
		super(properties, registryRef, registryEntry, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getInt(Lnet/minecraft/world/GameRules$Key;)I"))
	public void tickInject(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		int sleepingPlayerCount = sleepManager.getSleeping();
		// TODO: Don't assume the TPS is 20
		int secondsUntilAwake = Math.abs(
				SleepMath.calculateSecondsUntilAwake((int) worldProperties.getTimeOfDay() % DAY_LENGTH, nightTimeStepPerTick, 20));

		//Gets the remainder of the current time of day, as this number never actually resets each day(from my own testing)
		int ticksUntilAwake = SleepMath.calculateTicksUntilAwake((int) worldProperties.getTimeOfDay() % DAY_LENGTH);

		// Check if the night has (almost) ended and the weather should be skipped
		if (secondsUntilAwake <= 2 && shouldSkipWeather) {
			clearWeather();
			shouldSkipWeather = false;
		}

		// Check if anyone is sleeping
		if (sleepingPlayerCount <= 0) {
			return;
		}

		// Fetch config values and do calculations
		int playerCount = getPlayers().size();
		double sleepingRatio = (double) sleepingPlayerCount / playerCount;
		double sleepingPercentage = sleepingRatio * 100;

		nightTimeStepPerTick = SleepMath.calculateNightTimeStepPerTick(sleepingRatio, config.sleepSpeedMultiplier, nightTimeStepPerTick);
		nightTimeStepPerTickRounded = (int) Math.round(nightTimeStepPerTick);
		var isNight = SleepMath.isNightTime(worldProperties.getTimeOfDay());
		var nightDayOrThunderstormText = Text.translatable(
				String.format("%s.text.%s", MOD_ID, worldProperties.isThundering() ? "thunderstorm" : isNight ? "night" : "day"));

		int blockEntityTickSpeedMultiplier = (int) Math.round(config.blockEntityTickSpeedMultiplier);
		int chunkTickSpeedMultiplier = (int) Math.round(config.chunkTickSpeedMultiplier);
		int raidTickSpeedMultiplier = (int) Math.round(config.raidTickSpeedMultiplier);

		boolean doDayLightCycle = server.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE);
		int playersRequiredToSleepPercentage = server.getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
		double playersRequiredToSleepRatio = server.getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE) / 100;
		int playersRequiredToSleep = (int) Math.ceil(playersRequiredToSleepRatio * playerCount);

		// Check if the required percentage of players are sleeping
		if (sleepingPercentage < playersRequiredToSleepPercentage) {
			if (!config.sendNotEnoughPlayersSleepingMessage) {
				return;
			}

			for (ServerPlayerEntity player : players) {
				player.sendMessage(
						Text.translatable(String.format("%s.text.not_enough_players_sleeping_message", MOD_ID), sleepingPlayerCount,
								playerCount, playersRequiredToSleep, playerCount, nightDayOrThunderstormText
						), true);
			}

			return;
		}

		// Advance time
		if (doDayLightCycle) {
			worldProperties.setTimeOfDay(worldProperties.getTimeOfDay() + nightTimeStepPerTickRounded);
		}

		// Tick block entities
		for (int i = blockEntityTickSpeedMultiplier; i > 1; i--) {
			this.tickBlockEntities();
		}

		// Tick chunks
		for (int i = chunkTickSpeedMultiplier; i > 1; i--) {
			chunkManager.tick(shouldKeepTicking, true);
		}

		// Tick raid timers
		for (int i = raidTickSpeedMultiplier; i > 1; i--) {
			raidManager.tick();
		}

		// Send new time to all players in the overworld
		server.getPlayerManager().sendToDimension(
				new WorldTimeUpdateS2CPacket(worldProperties.getTime(), worldProperties.getTimeOfDay(), doDayLightCycle), getRegistryKey());

		// Check if players are still supposed to be sleeping, and send a HUD message if so
		if (secondsUntilAwake > 0) {
			shouldSkipWeather = true;

			if (config.sendSleepingMessage) {
				sleepMessage = Text.translatable(String.format("%s.text.sleep_message", MOD_ID), sleepingPlayerCount, playerCount).append(
						nightDayOrThunderstormText);

				if(isNight) {
					if (config.showTimeUntilDawn) {
						sleepMessage.append(Text.translatable(String.format("%s.text.time_until_dawn", MOD_ID), secondsUntilAwake));
					}
				} else {
					if(config.showTimeUntilDusk) {
						sleepMessage.append(Text.translatable(String.format("%s.text.time_until_dusk", MOD_ID), secondsUntilAwake));
					}
				}

				for (ServerPlayerEntity player : players) {
					player.sendMessage(sleepMessage, true);
				}
			}
		}

		int tickGrace = 30; //The amount of extra ticks for waking up - maybe useful in cases where TPS is low?
		//In my own testing, using just secondsUntilAwake <= 0 seemed to have a few seconds where
		//trying to sleep back in the bed would kick the player right out
		if (secondsUntilAwake <= 0 && ticksUntilAwake <= tickGrace) {
			this.wakeSleepingPlayers();
		}
	}

	@Inject(method = "tickTime", at = @At(value = "HEAD"), cancellable = true)
	public void tickTimeInject(CallbackInfo ci) {
		this.worldProperties.getScheduledEvents().processEvents(this.server, this.properties.getTime());

		if (!this.shouldTickTime) {
			ci.cancel();
			return;
		}

		long l = this.properties.getTime() + 1L;
		if (sleepManager.getSleeping() <= 0) {
			this.worldProperties.setTime(l);
		}

		if (tickDelay > 0L) {
			tickDelay -= 1L;
			server.getPlayerManager().sendToDimension(
					new WorldTimeUpdateS2CPacket(worldProperties.getTime(), worldProperties.getTimeOfDay(),
							this.properties.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)
					), getRegistryKey());

			ci.cancel();
			return;
		}

		if (sleepManager.getSleeping() > 0) {
			return;
		}

		if (this.properties.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
			this.worldProperties.setTimeOfDay(this.properties.getTimeOfDay() + 1L);
		}

		tickDelay = config.tickDelay;

		ci.cancel();
	}

	/**
	 * Cancels {@link ServerWorld#sendSleepingStatus sendSleepingStatus}.
	 *
	 * @author Steveplays28
	 * @reason Method's HUD messages conflict with Realistic Sleep's custom HUD messages.
	 */
	@SuppressWarnings("JavadocReference")
	@Inject(method = "sendSleepingStatus", at = @At(value = "HEAD"), cancellable = true)
	private void sendSleepingStatusInject(CallbackInfo ci) {
		ci.cancel();
	}

	@Unique
	private void clearWeather() {
		boolean doWeatherCycle = server.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE);

		if (doWeatherCycle && (worldProperties.isRaining() || worldProperties.isThundering())) {
			// Reset weather clock and clear weather
			var nextRainTime = (int) (DAY_LENGTH * SleepMath.getRandomNumberInRange(0.5, 7.5));
			worldProperties.setRainTime(nextRainTime);
			worldProperties.setThunderTime(nextRainTime + (Math.random() > 0 ? 1 : -1));

			worldProperties.setThundering(false);
			worldProperties.setRaining(false);
		}
	}
}
