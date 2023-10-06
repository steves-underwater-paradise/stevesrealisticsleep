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
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static RealisticSleepConfig config;

	@Override
	public void onInitialize() {
		LOGGER.info("[RealisticSleep] Loading!");
		AutoConfig.register(RealisticSleepConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(RealisticSleepConfig.class).getConfig();

		// Listen for when the server is reloading (i.e. /reload), and reload the config
		ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((s, m) -> {
			LOGGER.info("[Realistic Sleep] Reloading config!");
			AutoConfig.getConfigHolder(RealisticSleepConfig.class).load();
			config = AutoConfig.getConfigHolder(RealisticSleepConfig.class).getConfig();
		});

		//Registers a sleeping event using Fabric API
		EntitySleepEvents.ALLOW_SLEEP_TIME.register(((player, sleepingPos, vanillaResult) -> {
			//If day sleeping is disabled, then the vanilla Minecraft action will be returned instead
			if(config.allowDaySleeping) {
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		}));
	}
}
