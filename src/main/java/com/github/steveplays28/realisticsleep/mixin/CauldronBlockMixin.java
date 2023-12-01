package com.github.steveplays28.realisticsleep.mixin;

import com.github.steveplays28.realisticsleep.api.RealisticSleepApi;
import com.github.steveplays28.realisticsleep.util.CauldronUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.CauldronBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;

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
	private boolean modifyFillWithPrecipitationRequirements(boolean original, @Local World world, @Local BlockPos pos, @Local Biome.Precipitation precipitation) {
		return canBeFilledByPrecipitation(world, pos, precipitation);
	}

	@Unique
	private boolean canBeFilledByPrecipitation(World world, BlockPos pos, Biome.Precipitation precipitation) {
		if (CauldronUtil.canBeFilledByDripstone(world, pos)) {
			return false;
		}

		final var isSleeping = RealisticSleepApi.isSleeping(world);
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
