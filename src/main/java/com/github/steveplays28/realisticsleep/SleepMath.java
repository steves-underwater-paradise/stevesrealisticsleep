package com.github.steveplays28.realisticsleep;

public class SleepMath {
    public static final int DAY_LENGTH = 24000;
    public static final int CLEAR_AWAKE_TIME = 23460;
    public static final int RAIN_AWAKE_TIME = 23992;

//    private static double createCurve(double curveAggression, double playerPercentage) {
//        return curveAggression * playerPercentage / (curveAggression * playerPercentage * 2 - (curveAggression - playerPercentage) + 1);
//    }

    public static int calculateNightTimeStepPerTick(double sleepingRatio, int multiplier) {
        return (int) (sleepingRatio * multiplier);
    }

    public static int calculateTicksTo(int timeOfDay, int targetTimeOfDay) {
        if (timeOfDay > targetTimeOfDay)
            return targetTimeOfDay + DAY_LENGTH - timeOfDay;

        return targetTimeOfDay - timeOfDay;
    }

    public static int calculateTicksUntilAwake(int currentTimeOfDay) {
        // TODO: Take weather into account
        return calculateTicksTo(currentTimeOfDay, CLEAR_AWAKE_TIME);
    }

    public static int calculateSecondsUntilAwake(int currentTimeOfDay, int timeStepPerTick, int tps) {
        return (int) Math.round(calculateTicksUntilAwake(currentTimeOfDay) / (double) timeStepPerTick / tps);
    }

    public static double getRandomNumber(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }
}
