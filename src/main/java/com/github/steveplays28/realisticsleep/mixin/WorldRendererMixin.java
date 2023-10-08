package com.github.steveplays28.realisticsleep.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;

@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Shadow
	@Nullable
	private ClientWorld world;

	@ModifyConstant(method = "renderClouds(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Matrix4f;FDDD)V", constant = @Constant(floatValue = 0.03f))
	private float modifyCloudSpeed(float cloudSpeed) {
		if (world == null) {
			return cloudSpeed;
		}

		var players = world.getPlayers();
		var sleepingPlayers = players.stream().filter(LivingEntity::isSleeping);
		var playerCount = players.size();
		var sleepingPlayerCount = sleepingPlayers.count();
		double sleepingRatio = (double) sleepingPlayerCount / playerCount;
		double sleepingPercentage = sleepingRatio * 100;
		int playersRequiredToSleepPercentage = world.getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE);

		if (sleepingPercentage < playersRequiredToSleepPercentage) {
			return cloudSpeed;
		}

		return cloudSpeed * config.cloudSpeedMultiplier;
	}
}
