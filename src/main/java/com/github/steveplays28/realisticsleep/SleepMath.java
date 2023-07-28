package com.github.steveplays28.realisticsleep;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;

public class SleepMath {
	public static final int DAY_LENGTH = 24000;
	public static final int WAKE_UP_TIME = 23000;

	public static double calculateNightTimeStepPerTick(double sleepingRatio, double multiplier, double lastTimeStepPerTick) {
		return switch (config.sleepSpeedCurve) {
			case LINEAR -> sleepingRatio * multiplier;
		};
	}

	public static int calculateTicksToTimeOfDay(int timeOfDay, int targetTimeOfDay) {
		return targetTimeOfDay - timeOfDay;
	}

	public static int calculateTicksUntilAwake(int currentTimeOfDay) {
		return calculateTicksToTimeOfDay(currentTimeOfDay, DAY_LENGTH);
	}

	public static int calculateSecondsUntilAwake(int currentTimeOfDay, double timeStepPerTick, double tps) {
		return (int) Math.round(calculateTicksUntilAwake(currentTimeOfDay) / timeStepPerTick / tps);
	}

	public static double getRandomNumberInRange(double min, double max) {
		return (Math.random() * (max - min)) + min;
	}
}
