package com.github.steveplays28.realisticsleep.client;

import com.github.steveplays28.realisticsleep.config.RealisticSleepModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class RealisticSleepClient implements ClientModInitializer {
    public static RealisticSleepModConfig config = null;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(RealisticSleepModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(RealisticSleepModConfig.class).getConfig();
    }
}
