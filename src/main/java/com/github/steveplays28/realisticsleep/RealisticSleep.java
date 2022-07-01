package com.github.steveplays28.realisticsleep;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RealisticSleep implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("realisticsleep");
    public static Map<String, Object> config = new HashMap<>();

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile = new File(String.valueOf(FabricLoader.getInstance().getConfigDir()), "realisticsleep.json");

    @SuppressWarnings("unchecked")
    @Override
    public void onInitialize() {
        LOGGER.info("[RealisticSleep] Setting up...");

        // Ccheck if config file exists
        if (!configFile.exists()) {
            try {
                // Create empty config file
                if (configFile.createNewFile()) {
                    LOGGER.info("[RealisticSleep] Created a config file");

                    // Add default config to config file
                    config.put("dawnMessage", "The sun rises.");
                    config.put("sleepSpeedMultiplier", 25);
                    config.put("blockEntityTickSpeedMultiplier", 2);
                    config.put("chunkTickSpeedMultiplier", 2);
                    String json = gson.toJson(config);

                    FileWriter myWriter = new FileWriter(configFile);
                    myWriter.write(json);
                    myWriter.close();
                    LOGGER.info("[RealisticSleep] Successfully wrote default config to the config file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("[RealisticSleep] Config file already exists, loading existing config file...");
        }

        // Load config file
        try {
            config = gson.fromJson(new FileReader(configFile), HashMap.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
