package io.github.steveplays28.stevesrealisticsleep.forge.event;

import io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep.config;

@Mod.EventBusSubscriber(modid = StevesRealisticSleep.MOD_ID)
public class StevesRealisticSleepForgeEventHandler {
	@SubscribeEvent
	public static void onSleepingTimeCheck(@NotNull SleepingTimeCheckEvent event) {
		if (config.allowDaySleeping) {
			event.setResult(Event.Result.ALLOW);
		}
	}
}
