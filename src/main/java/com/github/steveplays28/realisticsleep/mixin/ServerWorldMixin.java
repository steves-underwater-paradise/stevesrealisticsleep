package com.github.steveplays28.realisticsleep.mixin;

import com.github.steveplays28.realisticsleep.SleepMath;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.SleepManager;
import net.minecraft.text.Text;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;
import static com.github.steveplays28.realisticsleep.SleepMath.DAY_LENGTH;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
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

	protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> registryEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
		super(properties, registryRef, registryEntry, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getInt(Lnet/minecraft/world/GameRules$Key;)I"))
	public void tickInject(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		// Check if anyone is sleeping
		int sleepingPlayerCount = sleepManager.getSleeping();

		if (sleepingPlayerCount <= 0) {
			return;
		}

		// Fetch values and do calculations
		int playerCount = server.getCurrentPlayerCount();
		double sleepingRatio = (double) sleepingPlayerCount / playerCount;
		double sleepingPercentage = sleepingRatio * 100;
		int nightTimeStepPerTick = SleepMath.calculateNightTimeStepPerTick(sleepingRatio, config.sleepSpeedMultiplier);
		int blockEntityTickSpeedMultiplier = (int) Math.round((double) config.blockEntityTickSpeedMultiplier);
		int chunkTickSpeedMultiplier = (int) Math.round((double) config.chunkTickSpeedMultiplier);
		boolean dayLightCycle = server.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE);
		int playersRequiredToSleepPercentage = server.getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
		double playersRequiredToSleepRatio = server.getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE) / 100;
		int playersRequiredToSleep = (int) Math.ceil(playersRequiredToSleepRatio * playerCount);

		// Check if the required percentage of players are sleeping
		if (sleepingPercentage < playersRequiredToSleepPercentage) {
			for (ServerPlayerEntity player : players) {
				player.sendMessage(Text.of(sleepingPlayerCount + "/" + playerCount + " players are currently sleeping. " + playersRequiredToSleep + "/" + playerCount + " players are required to sleep through the night."), true);
			}

			return;
		}

		// Advance time
		worldProperties.setTime(worldProperties.getTime() + nightTimeStepPerTick);
		if (dayLightCycle) {
			worldProperties.setTimeOfDay((worldProperties.getTimeOfDay() + nightTimeStepPerTick) % DAY_LENGTH);
		}

		// Tick block entities and chunks
		for (int i = blockEntityTickSpeedMultiplier; i > 1; i--) {
			this.tickBlockEntities();
		}

		for (int i = chunkTickSpeedMultiplier; i > 1; i--) {
			ChunkManager chunkManager = this.getChunkManager();
			chunkManager.tick(shouldKeepTicking, true);
		}

		// Send new time to all players in the overworld
		server.getPlayerManager().sendToDimension(new WorldTimeUpdateS2CPacket(worldProperties.getTime(), worldProperties.getTimeOfDay(), dayLightCycle), getRegistryKey());

		// Send HUD message to all players
		// TODO: Don't assume the TPS is 20
		int secondsUntilAwake = SleepMath.calculateSecondsUntilAwake((int) worldProperties.getTimeOfDay(), nightTimeStepPerTick, 20);
		int maxSecondsUntilAwake = SleepMath.calculateSecondsUntilAwake(DAY_LENGTH, nightTimeStepPerTick, 20);

		if (secondsUntilAwake < maxSecondsUntilAwake) {
			for (ServerPlayerEntity player : players) {
				if (worldProperties.isThundering()) {
					player.sendMessage(Text.of(sleepingPlayerCount + "/" + playerCount + " players are sleeping through this thunderstorm (time until dawn: " + secondsUntilAwake + "s)"), true);
				} else {
					player.sendMessage(Text.of(sleepingPlayerCount + "/" + playerCount + " players are sleeping through this night (time until dawn: " + secondsUntilAwake + "s)"), true);
				}
			}
		}

		// Check if it's dawn
		if (secondsUntilAwake <= 1) {
			// Advance days counter


			// Check if it's raining or thundering
			if (worldProperties.isRaining() || worldProperties.isThundering()) {
				// Clear weather and reset weather clock
				worldProperties.setThundering(false);
				worldProperties.setRaining(false);
				worldProperties.setClearWeatherTime((int) (DAY_LENGTH * SleepMath.getRandomNumber(1.25, 3)));
			}

			// Check if dawn message isn't set to nothing
			if (!config.dawnMessage.equals("")) {
				// Send HUD message to all players
				for (ServerPlayerEntity player : players) {
					player.sendMessage(Text.of(config.dawnMessage), true);
				}
			}
		}
	}

	/**
	 * @author Steveplays28
	 * @reason Method's HUD messages conflicts with my custom HUD messages
	 */
	@Overwrite
	private void sendSleepingStatus() {
	}
}
