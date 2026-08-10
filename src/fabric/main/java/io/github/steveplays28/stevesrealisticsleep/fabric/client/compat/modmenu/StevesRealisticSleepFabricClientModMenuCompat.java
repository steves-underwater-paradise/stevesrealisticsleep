package io.github.steveplays28.stevesrealisticsleep.fabric.client.compat.modmenu;

import io.github.steveplays28.stevesrealisticsleep.config.StevesRealisticSleepConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

public class StevesRealisticSleepFabricClientModMenuCompat implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(StevesRealisticSleepConfig.class, parent).get();
	}
}
