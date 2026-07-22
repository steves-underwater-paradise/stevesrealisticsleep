package io.github.steveplays28.stevesrealisticsleep.mixin.accessor;

import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractCauldronBlock.class)
public interface AbstractCauldronBlockAccessor {
	@Invoker
	void invokeScheduledTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPosition, Random random);
}
