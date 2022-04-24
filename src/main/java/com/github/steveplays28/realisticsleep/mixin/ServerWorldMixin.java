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
import java.util.Objects;
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

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> registryEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, registryEntry, profiler, isClient, debugWorld, seed);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getInt(Lnet/minecraft/world/GameRules$Key;)I"))
    private void tickInject(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        // Check if anyone is sleeping
        int sleepingPlayerCount = sleepManager.getSleeping();

        if (sleepingPlayerCount <= 0) {
            return;
        }

        // Fetch values and do calculations
        int playerCount = server.getCurrentPlayerCount();
        boolean dayLightCycle = server.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE);

        double sleepingRatio = (double) sleepingPlayerCount / playerCount;
        int nightTimeStepPerTick = SleepMath.calculateNightTimeStepPerTick(sleepingRatio, (int) Math.round((double) config.get("sleepSpeedModifier")));

        // Advance time
        worldProperties.setTime(worldProperties.getTime() + nightTimeStepPerTick);
        if (dayLightCycle) {
            worldProperties.setTimeOfDay((worldProperties.getTimeOfDay() + nightTimeStepPerTick) % DAY_LENGTH);
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
                    if (sleepingPlayerCount > 1) {
                        player.sendMessage(Text.of(sleepingPlayerCount + " players are sleeping through this thunderstorm (time until dawn: " + secondsUntilAwake + "s)"), true);
                    } else {
                        player.sendMessage(Text.of(sleepingPlayerCount + " player is sleeping through this thunderstorm (time until dawn: " + secondsUntilAwake + "s)"), true);
                    }
                } else if (dayLightCycle) {
                    if (sleepingPlayerCount > 1) {
                        player.sendMessage(Text.of(sleepingPlayerCount + " players are sleeping through this night (time until dawn: " + secondsUntilAwake + "s)"), true);
                    } else {
                        player.sendMessage(Text.of(sleepingPlayerCount + " player is sleeping through this night (time until dawn: " + secondsUntilAwake + "s)"), true);
                    }
                } else {
                    if (sleepingPlayerCount > 1) {
                        player.sendMessage(Text.of(sleepingPlayerCount + " players are sleeping (time until dawn: " + secondsUntilAwake + "s)"), true);
                    } else {
                        player.sendMessage(Text.of(sleepingPlayerCount + " player is sleeping (time until dawn: " + secondsUntilAwake + "s)"), true);
                    }
                }
            }
        }

        // Check if it's dawn
        if (secondsUntilAwake <= 1) {
            // Check if it's raining or thundering
            if (worldProperties.isRaining() || worldProperties.isThundering()) {
                // Clear weather and reset weather clock
                worldProperties.setThundering(false);
                worldProperties.setRaining(false);
                worldProperties.setClearWeatherTime((int) (DAY_LENGTH * SleepMath.getRandomNumber(1.25, 3)));
            }

            // Check if dawn message isn't set to nothing
            if (!Objects.equals(config.get("dawnMessage"), "")) {
                // Send HUD message to all players
                for (ServerPlayerEntity player : players) {
                    player.sendMessage(Text.of((String) config.get("dawnMessage")), true);
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
