package io.github.steveplays28.stevesrealisticsleep.mixin;

import io.github.steveplays28.stevesrealisticsleep.api.StevesRealisticSleepApi;
import io.github.steveplays28.stevesrealisticsleep.util.CauldronUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.CauldronBlock;
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
	private static float RAIN_FILL_CHANCE;
	@Shadow
	@Final
	private static float POWDER_SNOW_FILL_CHANCE;

	@SuppressWarnings("unused")
	@ModifyExpressionValue(method = "handlePrecipitation", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CauldronBlock;shouldHandlePrecipitation(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/biome/Biome$Precipitation;)Z"))
	private boolean stevesrealisticsleep$modifyFillWithPrecipitationRequirements(boolean original, @Local(argsOnly = true) @NotNull Level world, @Local(argsOnly = true) @NotNull BlockPos blockPosition, @Local(argsOnly = true) @NotNull Biome.Precipitation precipitation) {
		return stevesrealisticsleep$canBeFilledByPrecipitation(world, blockPosition, precipitation);
	}

	@Unique
	private boolean stevesrealisticsleep$canBeFilledByPrecipitation(@NotNull Level world, @NotNull BlockPos blockPosition, @NotNull Biome.Precipitation precipitation) {
		if (CauldronUtil.canBeFilledByDripstone(world, blockPosition)) {
			return false;
		}

		final var isSleeping = StevesRealisticSleepApi.isSleeping(world);
		final var fillWithRainChance = isSleeping ? RAIN_FILL_CHANCE * config.precipitationTickSpeedMultiplier : RAIN_FILL_CHANCE;
		final var fillWithSnowChance = isSleeping ? POWDER_SNOW_FILL_CHANCE * config.precipitationTickSpeedMultiplier : POWDER_SNOW_FILL_CHANCE;

		if (precipitation == Biome.Precipitation.RAIN) {
			return world.getRandom().nextFloat() < fillWithRainChance;
		} else if (precipitation == Biome.Precipitation.SNOW) {
			return world.getRandom().nextFloat() < fillWithSnowChance;
		} else {
			return false;
		}
	}
}
