package io.github.steveplays28.stevesrealisticsleep.client;

import io.github.steveplays28.stevesrealisticsleep.api.StevesRealisticSleepApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

import static io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep.config;

public class StevesRealisticSleepClient {
	public static void tickWorldRendererFasterWhileSleeping(ClientLevel world) {
		if (StevesRealisticSleepApi.isSleeping(world)) {
			for (int i = 0; i < config.worldRendererTickSpeedMultiplier; i++) {
				Minecraft.getInstance().levelRenderer.tick();
			}
		}
	}
}
