package com.github.steveplays28.realisticsleep;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealisticSleep implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("realisticsleep");

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world! Sleep mod go brr");
    }
}
