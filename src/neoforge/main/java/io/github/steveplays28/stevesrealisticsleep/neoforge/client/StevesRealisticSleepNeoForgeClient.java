package io.github.steveplays28.stevesrealisticsleep.neoforge.client;

import io.github.steveplays28.stevesrealisticsleep.client.StevesRealisticSleepClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(value = Dist.CLIENT)
public class StevesRealisticSleepNeoForgeClient {
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		ClientLevel clientLevel = Minecraft.getInstance().level;
		if (clientLevel != null) {
			StevesRealisticSleepClient.tickWorldRendererFasterWhileSleeping(clientLevel);
		}
	}
}
