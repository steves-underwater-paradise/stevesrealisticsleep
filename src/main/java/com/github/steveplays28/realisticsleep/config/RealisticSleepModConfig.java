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
    public double sleepSpeedMultiplier = 25.0;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    @ConfigEntry.Gui.Tooltip
    public int blockEntityTickSpeedMultiplier = 2;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    @ConfigEntry.Gui.Tooltip
    public int chunkTickSpeedMultiplier = 2;
}
