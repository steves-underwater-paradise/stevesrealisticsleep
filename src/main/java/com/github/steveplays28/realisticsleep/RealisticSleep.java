package com.github.steveplays28.realisticsleep;

import com.github.steveplays28.realisticsleep.config.RealisticSleepConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealisticSleep implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("realisticsleep");
	public static RealisticSleepConfig config;

	@Override
	public void onInitialize() {
		LOGGER.info("[RealisticSleep] Loading!");
		AutoConfig.register(RealisticSleepConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(RealisticSleepConfig.class).getConfig();
	}
}
