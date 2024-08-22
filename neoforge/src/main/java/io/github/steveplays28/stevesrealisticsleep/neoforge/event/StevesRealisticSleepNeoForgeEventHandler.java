package io.github.steveplays28.stevesrealisticsleep.neoforge.event;

import io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.SleepingTimeCheckEvent;
import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep.config;

@Mod.EventBusSubscriber(modid = StevesRealisticSleep.MOD_ID)
public class StevesRealisticSleepNeoForgeEventHandler {
	@SubscribeEvent
	public static void onSleepingTimeCheck(@NotNull SleepingTimeCheckEvent event) {
		if (config.allowDaySleeping) {
			event.setResult(Event.Result.ALLOW);
		}
	}
}
