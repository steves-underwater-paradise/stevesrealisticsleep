package io.github.steveplays28.stevesrealisticsleep.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import org.jetbrains.annotations.NotNull;

public class CauldronUtil {
	public static boolean canBeFilledByDripstone(@NotNull Level world, @NotNull BlockPos blockPosition) {
		return PointedDripstoneBlock.findStalactiteTipAboveCauldron(world, blockPosition) != null;
	}
}
