package io.github.steveplays28.stevesrealisticsleep.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(Block.class)
public interface BlockAccessor {
    @Invoker("randomTick")
    void invokeRandomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPosition, RandomSource randomSource);
}
