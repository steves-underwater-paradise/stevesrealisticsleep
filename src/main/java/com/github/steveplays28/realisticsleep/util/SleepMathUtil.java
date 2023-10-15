package com.github.steveplays28.realisticsleep.util;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;

public class SleepMathUtil {
	public static final int DAY_LENGTH = 24000;
	public static final int DAWN_WAKE_UP_TIME = 23449;
	public static final int DUSK_WAKE_UP_TIME = 12449;
	public static final double WAKE_UP_GRACE_PERIOD_TICKS = Math.max(config.sleepSpeedMultiplier, 20);
	public static final int MINIMUM_SLEEP_TICKS_TO_CLEAR_WEATHER = DAY_LENGTH / 10;

	public static double calculateTimeStepPerTick(double sleepingRatio, double multiplier, double lastTimeStepPerTick) {
		return switch (config.sleepSpeedCurve) {
			case LINEAR -> sleepingRatio * multiplier;
			case EXPONENTIAL -> Math.pow(lastTimeStepPerTick, 1 + sleepingRatio * multiplier);
		};
	}

	public static int calculateTicksToTimeOfDay(int timeOfDay, int targetTimeOfDay) {
		return targetTimeOfDay - timeOfDay;
	}

	public static int calculateTicksUntilAwake(int currentTimeOfDay) {
		return Math.abs(calculateTicksToTimeOfDay(currentTimeOfDay, isNightTime(currentTimeOfDay) ? DAWN_WAKE_UP_TIME : DUSK_WAKE_UP_TIME));
	}

	public static int calculateSecondsUntilAwake(int currentTimeOfDay, double timeStepPerTick, double tps) {
		return (int) Math.round(calculateTicksUntilAwake(currentTimeOfDay % DAY_LENGTH) / timeStepPerTick / tps);
	}

	public static double getRandomNumberInRange(double min, double max) {
		return (Math.random() * (max - min)) + min;
	}

	public static boolean isNightTime(long currentTimeOfDay) {
		return currentTimeOfDay > DUSK_WAKE_UP_TIME && currentTimeOfDay < DAWN_WAKE_UP_TIME;
	}
}
