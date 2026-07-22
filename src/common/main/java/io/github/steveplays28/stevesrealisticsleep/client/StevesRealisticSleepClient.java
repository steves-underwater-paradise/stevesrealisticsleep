package io.github.steveplays28.stevesrealisticsleep.client;

import dev.architectury.event.events.client.ClientTickEvent;
import io.github.steveplays28.stevesrealisticsleep.api.StevesRealisticSleepApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

import static io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep.config;

@Environment(EnvType.CLIENT)
public class StevesRealisticSleepClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvent.CLIENT_LEVEL_PRE.register(this::tickWorldRendererFasterWhileSleeping);
	}

	private void tickWorldRendererFasterWhileSleeping(ClientWorld world) {
		if (StevesRealisticSleepApi.isSleeping(world)) {
			for (int i = 0; i < config.worldRendererTickSpeedMultiplier; i++) {
				MinecraftClient.getInstance().worldRenderer.tick();
			}
		}
	}
}
