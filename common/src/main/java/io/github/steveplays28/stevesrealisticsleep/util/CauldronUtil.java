package io.github.steveplays28.stevesrealisticsleep.util;

import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CauldronUtil {
	public static boolean canBeFilledByDripstone(World world, BlockPos pos) {
		BlockPos blockPos = PointedDripstoneBlock.getDripPos(world, pos);
		return blockPos != null;
	}
}
