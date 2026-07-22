package io.github.steveplays28.stevesrealisticsleep.fabric.event.entity;

import net.minecraft.util.ActionResult;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;

import static io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep.config;

public class StevesRealisticSleepFabricEventHandlerRegistry {
	public static void initialize() {
		// Register custom sleep time (allows daytime sleeping)
		EntitySleepEvents.ALLOW_SLEEP_TIME.register(((player, sleepingPos, vanillaResult) -> {
			if (config.allowDaySleeping) {
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		}));
	}
}
