package com.github.steveplays28.realisticsleep.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "realisticsleep")
public class RealisticSleepConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	public boolean sendDawnMessage = true;
	@ConfigEntry.Gui.Tooltip
	public String dawnMessage = "The sun rises.";

	@ConfigEntry.Gui.Tooltip
	public boolean sendDuskMessage = true;
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
	public boolean allowDaySleeping = false;

	@ConfigEntry.Gui.Tooltip
	public SleepSpeedCurve sleepSpeedCurve = SleepSpeedCurve.LINEAR;
	@ConfigEntry.Gui.Tooltip
	public double sleepSpeedMultiplier = 25d;
	@ConfigEntry.Gui.Tooltip
	public double blockEntityTickSpeedMultiplier = 25d;
	@ConfigEntry.Gui.Tooltip
	public double chunkTickSpeedMultiplier = 1d;
	@ConfigEntry.Gui.Tooltip
	public double raidTickSpeedMultiplier = 25d;
	@ConfigEntry.Gui.Tooltip
	public double fluidScheduledTickSpeedMultiplier = 25d;
	@ConfigEntry.Gui.Tooltip
	public double thunderTickSpeedMultiplier = 25d;
	@ConfigEntry.Gui.Tooltip
	public double iceAndSnowTickSpeedMultiplier = 25d;
	@ConfigEntry.Gui.Tooltip
	public double cropGrowthTickSpeedMultiplier = 25d;
	@ConfigEntry.Gui.Tooltip
	public double precipitationTickSpeedMultiplier = 25d;
	@ConfigEntry.Gui.Tooltip
	public double blockRandomTickSpeedMultiplier = 1d;
	@ConfigEntry.Gui.Tooltip
	public double fluidRandomTickSpeedMultiplier = 1d;
	@ConfigEntry.Gui.Tooltip
	public float cloudSpeedMultiplier = 25f;

	@ConfigEntry.Gui.Tooltip
	public long tickDelay = -1;

	public enum SleepSpeedCurve {
		LINEAR, EXPONENTIAL
	}
}
