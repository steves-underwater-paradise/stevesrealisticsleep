package io.github.steveplays28.stevesrealisticsleep.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

// TODO: Seperate into another mixin for >=1.21.11 or something like that and also mix into net/minecraft/world/timeline/Timelines to make the EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE contant (addModifierTrack)
// TODO: New target for >=1.21.11: "net/minecraft/world/entity/animal/feline/Cat$CatRelaxOnOwnerGoal"
@Mixin(targets = "net/minecraft/world/entity/animal/Cat$CatRelaxOnOwnerGoal")
public class CatEntityMixin {
	@Unique
	private static final float stevesrealisticsleep$ALLOWED_TIME_OF_DAY_FOR_GIFT_DROPS = 0.78f;
	/**
	* Modifies {@link net.minecraft.world.entity.animal.Cat$CatRelaxOnOwnerGoal#stop stop} to never check the sky angle before dropping gifts.
	*/
	@ModifyExpressionValue(method = "stop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getTimeOfDay(F)F"))
	private float stevesrealisticsleep$preventUseOfTimeOfDayForGiftDrops(float original) {
		return stevesrealisticsleep$ALLOWED_TIME_OF_DAY_FOR_GIFT_DROPS;
	}
}
