package io.github.steveplays28.stevesrealisticsleep.api;

import io.github.steveplays28.stevesrealisticsleep.extension.ServerWorldExtension;
import io.github.steveplays28.stevesrealisticsleep.mixin.accessor.ServerWorldAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static io.github.steveplays28.stevesrealisticsleep.util.SleepMathUtil.DAY_LENGTH;
import static io.github.steveplays28.stevesrealisticsleep.util.SleepMathUtil.calculateTicksUntilAwake;

/**
 * Realistic Sleep's API. This class contains methods and ways to grab information from Realistic Sleep that may be useful to other mods.
 */
public class StevesRealisticSleepApi {
	/**
	 * @param world The world (aka dimension)
	 * @return The sleep progress of the world as a percentage.
	 * @since v1.8.4
	 */
	@SuppressWarnings("unused")
	public static float getSleepProgress(@NotNull Level world) {
		return ((float) (DAY_LENGTH - calculateTicksUntilAwake(getTimeOfDay(world))) / DAY_LENGTH) * 100f;
	}

	/**
	 * @param world The world (aka dimension)
	 * @return The time of day of the world in ticks, with the day count filtered out using a modulo operator. This means that when Minecraft returns the time of day of a world, the day count doesn't have any effect on the resulting time. Equal to <code>(int) world.getLevelData().getDayTime() % DAY_LENGTH</code>.
	 * @since v1.8.4
	 */
	public static int getTimeOfDay(@NotNull Level world) {
		return (int) world.getLevelData().getDayTime() % DAY_LENGTH;
	}

	/**
	 * @param world The world (aka dimension)
	 * @return Whether players are sleeping in this world (time is passing at an accelerated rate). The world will be casted to either a <code>ServerLevel</code> or <code>ClientLevel</code> depending on the side this method is called from. Prefer the sides' respective API methods instead.
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean isSleeping(@NotNull Level world) {
		if (world.isClientSide()) {
			return isSleeping((ClientLevel) world);
		} else {
			return isSleeping((ServerLevel) world);
		}
	}

	/**
	 * @param world The clientside world (aka dimension)
	 * @return Whether players are sleeping in this world (time is passing at an accelerated rate).
	 */
	public static boolean isSleeping(@NotNull ClientLevel world) {
		var players = world.players();
		var sleepingPlayers = players.stream().filter(LivingEntity::isSleeping);
		var playerCount = players.size();
		var sleepingPlayerCount = sleepingPlayers.count();
		double sleepingRatio = (double) sleepingPlayerCount / playerCount;
		double sleepingPercentage = sleepingRatio * 100;
		int playersRequiredToSleepPercentage = world.getGameRules().getInt(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE);

		return sleepingPercentage >= playersRequiredToSleepPercentage;
	}

	/**
	 * @param world The serverside world (aka dimension)
	 * @return Whether players are sleeping in this world (time is passing at an accelerated rate).
	 */
	public static boolean isSleeping(@NotNull ServerLevel world) {
		var playerCount = world.players().size();
		var sleepingPlayerCount = ((ServerWorldAccessor) world).getSleepStatus().amountSleeping();
		double sleepingRatio = (double) sleepingPlayerCount / playerCount;
		double sleepingPercentage = sleepingRatio * 100d;
		int playersRequiredToSleepPercentage = world.getGameRules().getInt(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE);

		return sleepingPercentage >= playersRequiredToSleepPercentage;
	}

	/**
	 * @param world The serverside world (aka dimension)
	 * @return The consecutive amount of sleep ticks. Returns <code>0</code> when no players are sleeping.
	 */
	@SuppressWarnings("unused")
	public static int getConsecutiveSleepTicks(ServerLevel world) {
		return ((ServerWorldExtension) world).consecutiveSleepTicks;
	}
}
