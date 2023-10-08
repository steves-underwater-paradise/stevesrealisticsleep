package com.github.steveplays28.realisticsleep.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "realisticsleep")
public class RealisticSleepConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	public boolean sendDawnMessage = true;

	@ConfigEntry.Gui.Tooltip
	public boolean sendDuskMessage = true;

	@ConfigEntry.Gui.Tooltip
	public String dawnMessage = "The sun rises.";

	@ConfigEntry.Gui.Tooltip
	public String duskMessage = "The night begins.";

	@ConfigEntry.Gui.Tooltip
	public boolean sendSleepingMessage = true;
	@ConfigEntry.Gui.Tooltip
	public boolean showTimeUntilDawn = true;

	@ConfigEntry.Gui.Tooltip
	public boolean showTimeUntilDusk = true;
	@ConfigEntry.Gui.Tooltip
	public boolean sendNotEnoughPlayersSleepingMessage = true;

	@ConfigEntry.Gui.Tooltip
	public boolean allowDaySleeping = true;

	@ConfigEntry.Gui.Tooltip
	public SleepSpeedCurve sleepSpeedCurve = SleepSpeedCurve.LINEAR;
	@ConfigEntry.Gui.Tooltip
	public double sleepSpeedMultiplier = 25;
	@ConfigEntry.Gui.Tooltip
	public double blockEntityTickSpeedMultiplier = 25;
	@ConfigEntry.Gui.Tooltip
	public double chunkTickSpeedMultiplier = 1;
	@ConfigEntry.Gui.Tooltip
	public double raidTickSpeedMultiplier = 25;

	@ConfigEntry.Gui.Tooltip
	public long tickDelay = -1;

	public enum SleepSpeedCurve {
		@ConfigEntry.Gui.Tooltip
		LINEAR
	}
}
