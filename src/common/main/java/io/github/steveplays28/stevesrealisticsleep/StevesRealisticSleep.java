package io.github.steveplays28.stevesrealisticsleep;

import io.github.steveplays28.stevesrealisticsleep.config.StevesRealisticSleepConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StevesRealisticSleep {
	public static final String MOD_ID = "stevesrealisticsleep";
	public static final String MOD_NAMESPACE = "steves_realistic_sleep";
	public static final String MOD_NAME = "Steve's Realistic Sleep";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static StevesRealisticSleepConfig config;

	public static void initialize() {
		LOGGER.info("Loading {}.", MOD_NAME);

		// TODO: Migrate config from `config/realisticsleep.json` to `config/steves_realistic_sleep.json`
		// TODO: Switch to YetAnotherConfigLib
		AutoConfig.register(StevesRealisticSleepConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(StevesRealisticSleepConfig.class).getConfig();

		// Listen for when the server is reloading (i.e. /reload), and reload the config
		// TODO: Move into a `/steves_realistic_sleep config reload` command
//		LifecycleEvent.START_DATA_PACK_RELOAD.register((s, m) -> {
//			LOGGER.info("Reloading {} config.", MOD_NAME);
//			AutoConfig.getConfigHolder(StevesRealisticSleepConfig.class).load();
//			config = AutoConfig.getConfigHolder(StevesRealisticSleepConfig.class).getConfig();
//		});
	}
}
