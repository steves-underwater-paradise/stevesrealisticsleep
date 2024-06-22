package io.github.steveplays28.stevesrealisticsleep.client.compat.modmenu;

import io.github.steveplays28.stevesrealisticsleep.config.StevesRealisticSleepConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class StevesRealisticSleepClientModMenuCompat implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(StevesRealisticSleepConfig.class, parent).get();
	}
}
