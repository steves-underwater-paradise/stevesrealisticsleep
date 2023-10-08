package com.github.steveplays28.realisticsleep;

import com.github.steveplays28.realisticsleep.config.RealisticSleepConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealisticSleep implements ModInitializer {
	public static final String MOD_ID = "realisticsleep";
	public static final String MOD_NAME = "Realistic Sleep";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static RealisticSleepConfig config;

	@Override
	public void onInitialize() {
		LOGGER.info("Loading {}.", MOD_NAME);
		AutoConfig.register(RealisticSleepConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(RealisticSleepConfig.class).getConfig();

		// Listen for when the server is reloading (i.e. /reload), and reload the config
		ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((s, m) -> {
			LOGGER.info("Reloading {} config.", MOD_NAME);
			AutoConfig.getConfigHolder(RealisticSleepConfig.class).load();
			config = AutoConfig.getConfigHolder(RealisticSleepConfig.class).getConfig();
		});

		// Register custom sleep time (allows daytime sleeping)
		EntitySleepEvents.ALLOW_SLEEP_TIME.register(((player, sleepingPos, vanillaResult) -> {
			if (config.allowDaySleeping) {
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		}));
	}
}
