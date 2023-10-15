package com.github.steveplays28.realisticsleep.mixin;

import com.github.steveplays28.realisticsleep.api.RealisticSleepApi;
import net.minecraft.block.CauldronBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;

@Mixin(CauldronBlock.class)
public class CauldronBlockMixin {
	@Shadow
	@Final
	private static float FILL_WITH_RAIN_CHANCE;

	@Shadow
	@Final
	private static float FILL_WITH_SNOW_CHANCE;

	@Inject(method = "canFillWithPrecipitation", at = @At(value = "HEAD"), cancellable = true)
	private static void canFillWithPrecipitationInject(World world, Biome.Precipitation precipitation, CallbackInfoReturnable<Boolean> cir) {
		if (!RealisticSleepApi.isSleeping(world)) {
			return;
		}

		if (precipitation == Biome.Precipitation.RAIN) {
			cir.setReturnValue(world.getRandom().nextFloat() < FILL_WITH_RAIN_CHANCE * config.precipitationTickSpeedMultiplier);
		} else if (precipitation == Biome.Precipitation.SNOW) {
			cir.setReturnValue(world.getRandom().nextFloat() < FILL_WITH_SNOW_CHANCE * config.precipitationTickSpeedMultiplier);
		}
	}
}
