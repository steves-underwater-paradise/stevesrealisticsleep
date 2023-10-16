package com.github.steveplays28.realisticsleep.mixin.sodium;

import com.github.steveplays28.realisticsleep.api.RealisticSleepApi;
import me.jellysquid.mods.sodium.client.render.immediate.CloudRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;

@Environment(EnvType.CLIENT)
@Mixin(CloudRenderer.class)
public class SodiumCloudRendererMixin {
	@ModifyVariable(method = "render", at = @At(value = "STORE"), name = "cloudTime", remap = false)
	private double renderModifyCloudTimeVariable(double cloudTime) {
		var world = MinecraftClient.getInstance().world;
		if (world == null) {
			return cloudTime;
		}

		return RealisticSleepApi.isSleeping(world) ? cloudTime * config.cloudSpeedMultiplier : cloudTime;
	}
}
