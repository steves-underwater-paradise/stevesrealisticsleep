package com.github.steveplays28.realisticsleep.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "realisticsleep")
public class RealisticSleepConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public String dawnMessage = "The sun rises.";
    @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
    @ConfigEntry.Gui.Tooltip
    public int sleepSpeedMultiplier = 25;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
    @ConfigEntry.Gui.Tooltip
    public int blockEntityTickSpeedMultiplier = 25;
    @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
    @ConfigEntry.Gui.Tooltip
    public int chunkTickSpeedMultiplier = 25;
}
