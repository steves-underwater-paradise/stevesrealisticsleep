package io.github.steveplays28.stevesrealisticsleep.mixin;

import io.github.steveplays28.stevesrealisticsleep.api.StevesRealisticSleepApi;
import io.github.steveplays28.stevesrealisticsleep.util.CauldronUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.CauldronBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep.config;

@Mixin(CauldronBlock.class)
public class CauldronBlockMixin {
	@Shadow
	@Final
	private static float FILL_WITH_RAIN_CHANCE;
	@Shadow
	@Final
	private static float FILL_WITH_SNOW_CHANCE;

	@SuppressWarnings("unused")
	@ModifyExpressionValue(method = "precipitationTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CauldronBlock;canFillWithPrecipitation(Lnet/minecraft/world/World;Lnet/minecraft/world/biome/Biome$Precipitation;)Z"))
	private boolean stevesrealisticsleep$modifyFillWithPrecipitationRequirements(boolean original, @Local(argsOnly = true) @NotNull World world, @Local(argsOnly = true) @NotNull BlockPos blockPosition, @Local(argsOnly = true) @NotNull Biome.Precipitation precipitation) {
		return stevesrealisticsleep$canBeFilledByPrecipitation(world, blockPosition, precipitation);
	}

	@Unique
	private boolean stevesrealisticsleep$canBeFilledByPrecipitation(@NotNull World world, @NotNull BlockPos blockPosition, @NotNull Biome.Precipitation precipitation) {
		if (CauldronUtil.canBeFilledByDripstone(world, blockPosition)) {
			return false;
		}

		final var isSleeping = StevesRealisticSleepApi.isSleeping(world);
		final var fillWithRainChance = isSleeping ? FILL_WITH_RAIN_CHANCE * config.precipitationTickSpeedMultiplier : FILL_WITH_RAIN_CHANCE;
		final var fillWithSnowChance = isSleeping ? FILL_WITH_SNOW_CHANCE * config.precipitationTickSpeedMultiplier : FILL_WITH_SNOW_CHANCE;

		if (precipitation == Biome.Precipitation.RAIN) {
			return world.getRandom().nextFloat() < fillWithRainChance;
		} else if (precipitation == Biome.Precipitation.SNOW) {
			return world.getRandom().nextFloat() < fillWithSnowChance;
		} else {
			return false;
		}
	}
}
