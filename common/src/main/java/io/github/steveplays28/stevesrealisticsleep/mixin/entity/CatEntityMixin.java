package io.github.steveplays28.stevesrealisticsleep.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/entity/passive/CatEntity$SleepWithOwnerGoal")
public class CatEntityMixin {
	@Unique
	private static final float stevesrealisticsleep$ALLOWED_SKY_ANGLE_FOR_GIFT_DROPS = 0.78f;

	@ModifyExpressionValue(method = "stop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getSkyAngle(F)F"))
	private float stevesrealisticsleep$preventUseOfSkyAngleForGiftDrops(float original) {
		return stevesrealisticsleep$ALLOWED_SKY_ANGLE_FOR_GIFT_DROPS;
	}
}
