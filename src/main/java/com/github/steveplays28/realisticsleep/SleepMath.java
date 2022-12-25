package com.github.steveplays28.realisticsleep;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;

public class SleepMath {
	public static final int DAY_LENGTH = 24000;
	public static final int AWAKE_TIME = 24000;

	public static double calculateNightTimeStepPerTick(double sleepingRatio, double multiplier, double lastTimeStepPerTick) {
		return switch (config.sleepSpeedCurve) {
			case LINEAR -> sleepingRatio * multiplier;
			case EXPONENTIAL -> calculateNightTimeStepPerTickExponential(sleepingRatio, multiplier, lastTimeStepPerTick);
		};
	}

	public static int calculateTicksToTimeOfDay(int timeOfDay, int targetTimeOfDay) {
		return targetTimeOfDay - timeOfDay;
	}

	public static int calculateTicksUntilAwake(int currentTimeOfDay) {
		return calculateTicksToTimeOfDay(currentTimeOfDay, AWAKE_TIME);
	}

	public static int calculateSecondsUntilAwake(int currentTimeOfDay, double timeStepPerTick, double tps) {
		RealisticSleep.LOGGER.info("currentTimeOfDay: {}", currentTimeOfDay);
		RealisticSleep.LOGGER.info("timeStepPerTick: {}", timeStepPerTick);
		RealisticSleep.LOGGER.info("tps: {}", tps);

		return (int) Math.round(calculateTicksUntilAwake(currentTimeOfDay) / timeStepPerTick / tps);
	}

	public static double getRandomNumberInRange(double min, double max) {
		return (Math.random() * (max - min)) + min;
	}

	public static double calculateNightTimeStepPerTickExponential(double sleepingRatio, double multiplier, double lastTimeStepPerTick) {
//		RealisticSleep.LOGGER.info("sleepingRatio: {}", sleepingRatio);
//		RealisticSleep.LOGGER.info("multiplier: {}", multiplier);
//		RealisticSleep.LOGGER.info("lastTimeStepPerTick: {}", lastTimeStepPerTick);

		if (lastTimeStepPerTick <= 1) {
			lastTimeStepPerTick = 2;
		}

		return Math.pow(lastTimeStepPerTick, sleepingRatio * multiplier);
	}
}
