package io.github.steveplays28.stevesrealisticsleep.fabric;

import io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep;
import io.github.steveplays28.stevesrealisticsleep.fabric.event.entity.StevesRealisticSleepFabricEventHandlerRegistry;
import net.fabricmc.api.ModInitializer;

public class StevesRealisticSleepFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		StevesRealisticSleep.initialize();
		StevesRealisticSleepFabricEventHandlerRegistry.initialize();
	}
}
