package com.github.steveplays28.realisticsleep.client.compat;

import com.github.steveplays28.realisticsleep.api.RealisticSleepApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;

@Environment(EnvType.CLIENT)
public class RealisticSleepClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.START_WORLD_TICK.register(this::tickWorldRendererFasterWhileSleeping);
	}

	private void tickWorldRendererFasterWhileSleeping(ClientWorld world) {
		if (RealisticSleepApi.isSleeping(world)) {
			for (int i = 0; i < config.worldRendererTickSpeedMultiplier; i++) {
				MinecraftClient.getInstance().worldRenderer.tick();
			}
		}
	}
}
