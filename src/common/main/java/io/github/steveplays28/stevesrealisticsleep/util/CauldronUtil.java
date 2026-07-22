package io.github.steveplays28.stevesrealisticsleep.util;

import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class CauldronUtil {
	public static boolean canBeFilledByDripstone(@NotNull World world, @NotNull BlockPos blockPosition) {
		return PointedDripstoneBlock.getDripPos(world, blockPosition) != null;
	}
}
