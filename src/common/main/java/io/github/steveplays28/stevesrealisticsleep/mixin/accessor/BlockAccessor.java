package io.github.steveplays28.stevesrealisticsleep.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(Block.class)
public interface BlockAccessor {
    @Invoker("animateTick")
    void invokeAnimateTick(BlockState blockState, Level level, BlockPos blockPosition, RandomSource randomSource);
}
