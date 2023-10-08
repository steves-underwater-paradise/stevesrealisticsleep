package com.github.steveplays28.realisticsleep.util;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;

public class SleepMathUtil {
	public static final int DAY_LENGTH = 24000;
	public static final int SUNRISE_WAKE_UP = 23449;
	public static final int SUNSET_WAKE_UP = 12449;

	public static double calculateNightTimeStepPerTick(double sleepingRatio, double multiplier, double lastTimeStepPerTick) {
		return switch (config.sleepSpeedCurve) {
			case LINEAR -> sleepingRatio * multiplier;
		};
	}

	public static int calculateTicksToTimeOfDay(int timeOfDay, int targetTimeOfDay) {
		return targetTimeOfDay - timeOfDay;
	}

	public static int calculateTicksUntilAwake(int currentTimeOfDay) {
		return calculateTicksToTimeOfDay(currentTimeOfDay, isNightTime(currentTimeOfDay) ? SUNRISE_WAKE_UP : SUNSET_WAKE_UP);
	}

	public static int calculateSecondsUntilAwake(int currentTimeOfDay, double timeStepPerTick, double tps) {
		return (int) Math.round(calculateTicksUntilAwake(currentTimeOfDay % DAY_LENGTH) / timeStepPerTick / tps);
	}

	public static double getRandomNumberInRange(double min, double max) {
		return (Math.random() * (max - min)) + min;
	}

	public static boolean isNightTime(long currentTimeOfDay) {
		return currentTimeOfDay % DAY_LENGTH >= SUNSET_WAKE_UP;
	}
}
