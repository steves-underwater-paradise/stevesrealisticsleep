package io.github.steveplays28.stevesrealisticsleep.neoforge.event;

import io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep;
import net.minecraft.entity.player.PlayerEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep.config;

@EventBusSubscriber(modid = StevesRealisticSleep.MOD_ID)
public class StevesRealisticSleepNeoForgeEventHandler {
	@SubscribeEvent
	public static void onCanPlayerSleep(@NotNull CanPlayerSleepEvent event) {
		if (!config.allowDaySleeping) {
			return;
		}

		@Nullable var vanillaProblem = event.getVanillaProblem();
		if (vanillaProblem == null || !vanillaProblem.equals(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW)) {
			return;
		}

		event.setProblem(null);
	}
}
