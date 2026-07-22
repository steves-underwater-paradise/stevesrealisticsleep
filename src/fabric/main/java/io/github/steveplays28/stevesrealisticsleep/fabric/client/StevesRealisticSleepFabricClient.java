package io.github.steveplays28.stevesrealisticsleep.fabric.client;

import io.github.steveplays28.stevesrealisticsleep.client.StevesRealisticSleepClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class StevesRealisticSleepFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientTickEvents.START_WORLD_TICK.register(
			StevesRealisticSleepClient::tickWorldRendererFasterWhileSleeping
		);
	}
}
