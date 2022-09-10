package com.github.steveplays28.realisticsleep.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class RealisticSleepAutoConfig {
    public RealisticSleepAutoConfig() {
        AutoConfig.register(RealisticSleepConfigData.class, GsonConfigSerializer::new);
    }
}
