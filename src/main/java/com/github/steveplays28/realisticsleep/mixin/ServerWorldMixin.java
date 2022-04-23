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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static com.github.steveplays28.realisticsleep.SleepMath.DAY_LENGTH;
import static com.github.steveplays28.realisticsleep.client.RealisticSleepClient.config;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    @Shadow
    @Final
    private ServerWorldProperties worldProperties;
    @Shadow
    @Final
    private List<ServerPlayerEntity> players;
    @Shadow
    @Final
    private MinecraftServer server;
    @Shadow
    @Final
    private SleepManager sleepManager;

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> registryEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, registryEntry, profiler, isClient, debugWorld, seed);
    }

    @Invoker("wakeSleepingPlayers")
    public abstract void invokeWakeSleepingPlayers();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getInt(Lnet/minecraft/world/GameRules$Key;)I"))
    private void tickInject(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        // Check if anyone is sleeping
        if (sleepManager.getSleeping() <= 0) {
            return;
        }

        // Fetch DoDaylightCycle gamerule and do calculations
        boolean dayLightCycle = server.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE);
        int sleepingPercentage = sleepManager.getSleeping() / server.getCurrentPlayerCount() * 100;
        int timeStepPerTick = SleepMath.calculateTimeStepPerTick(config.sleepSpeedModifier, 0.5, sleepingPercentage);

        // Advance time
        worldProperties.setTime(worldProperties.getTime() + timeStepPerTick);
        if (dayLightCycle) {
            worldProperties.setTimeOfDay((worldProperties.getTimeOfDay() + timeStepPerTick) % DAY_LENGTH);
        }

        // Send new time to all players in the overworld
        server.getPlayerManager().sendToDimension(new WorldTimeUpdateS2CPacket(worldProperties.getTime(), worldProperties.getTimeOfDay(), dayLightCycle), getRegistryKey());

        // Send HUD message to all players
        // TODO: Don't assume the TPS is 20
        int secondsUntilAwake = SleepMath.calculateSecondsUntilAwake((int) worldProperties.getTimeOfDay(), timeStepPerTick, 20);
        int maxSecondsUntilAwake = SleepMath.calculateSecondsUntilAwake(DAY_LENGTH, timeStepPerTick, 20);

        if (secondsUntilAwake < maxSecondsUntilAwake) {
            for (ServerPlayerEntity player : players) {
                if (worldProperties.isThundering()) {
                    if (sleepManager.getSleeping() > 1) {
                        player.sendMessage(Text.of(sleepManager.getSleeping() + " players are sleeping through this thunderstorm (time until dawn: " + secondsUntilAwake + "s)"), true);
                    } else {
                        player.sendMessage(Text.of(sleepManager.getSleeping() + " player is sleeping through this thunderstorm (time until dawn: " + secondsUntilAwake + "s)"), true);
                    }
                } else if (dayLightCycle) {
                    if (sleepManager.getSleeping() > 1) {
                        player.sendMessage(Text.of(sleepManager.getSleeping() + " players are sleeping through this night (time until dawn: " + secondsUntilAwake + "s)"), true);
                    } else {
                        player.sendMessage(Text.of(sleepManager.getSleeping() + " player is sleeping through this night (time until dawn: " + secondsUntilAwake + "s)"), true);
                    }
                } else {
                    if (sleepManager.getSleeping() > 1) {
                        player.sendMessage(Text.of(sleepManager.getSleeping() + " players are sleeping (time until dawn: " + secondsUntilAwake + "s)"), true);
                    } else {
                        player.sendMessage(Text.of(sleepManager.getSleeping() + " player is sleeping (time until dawn: " + secondsUntilAwake + "s)"), true);
                    }
                }
            }
        }

        // Check if it's dawn
        if (secondsUntilAwake <= 1) {
            // Clear weather
            worldProperties.setThundering(false);
            worldProperties.setRaining(false);
            worldProperties.setClearWeatherTime((int) (DAY_LENGTH * SleepMath.getRandomNumber(1.25, 3)));

            // Send HUD message to all players
            for (ServerPlayerEntity player : players) {
                player.sendMessage(Text.of(config.dawnMessage), true);
            }
        }
    }
}
