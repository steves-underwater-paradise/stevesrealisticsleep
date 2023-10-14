package com.github.steveplays28.realisticsleep.api;

import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.github.steveplays28.realisticsleep.util.SleepMathUtil.DAY_LENGTH;
import static com.github.steveplays28.realisticsleep.util.SleepMathUtil.calculateTicksUntilAwake;

/**
 * Realistic Sleep's API. This class contains methods and ways to grab information from Realistic Sleep that may be useful to other mods.
 */
public class RealisticSleepApi {
	/**
	 * @param world The world (aka dimension)
	 * @return The sleep progress of the world as a percentage.
	 * @since v1.8.4
	 */
	public static float getSleepProgress(@NotNull World world) {
		return ((float) (DAY_LENGTH - calculateTicksUntilAwake(getTimeOfDay(world))) / DAY_LENGTH) * 100f;
	}

	/**
	 * @param world The world (aka dimension)
	 * @return The time of day of the world in ticks, with the day count filtered out using a modulo operator. This means that when Minecraft returns the time of day of a world, the day count doesn't have any effect on the resulting time. Equal to <code>(int) world.getLevelProperties().getTimeOfDay() % DAY_LENGTH</code>.
	 * @since v1.8.4
	 */
	public static int getTimeOfDay(@NotNull World world) {
		return (int) world.getLevelProperties().getTimeOfDay() % DAY_LENGTH;
	}
}
