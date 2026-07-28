package io.github.steveplays28.stevesrealisticsleep.neoforge;

import io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(StevesRealisticSleep.MOD_ID)
public class StevesRealisticSleepNeoForge {
	public StevesRealisticSleepNeoForge(ModContainer modContainer) {
		StevesRealisticSleep.initialize();

		if (FMLEnvironment.dist == Dist.CLIENT) {
			io.github.steveplays28.stevesrealisticsleep.neoforge.client.StevesRealisticSleepNeoForgeClient.initialize(modContainer);
		}
	}
}
