package com.github.steveplays28.realisticsleep.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Config(name = "realisticsleep")
public class RealisticSleepModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public String dawnMessage = "The sun rises.";
    @ConfigEntry.Gui.Tooltip
    public int sleepSpeedModifier = 25;
}
