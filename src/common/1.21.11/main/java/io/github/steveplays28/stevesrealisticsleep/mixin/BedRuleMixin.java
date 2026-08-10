package io.github.steveplays28.stevesrealisticsleep.mixin;

import net.minecraft.world.attribute.BedRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep;

@Mixin(BedRule.class)
public abstract class BedRuleMixin {
	@ModifyReturnValue(method = "canSleep", at = @At("RETURN"))
	private boolean stevesrealisticsleep$allowDaySleeping(boolean canSleep) {
		return StevesRealisticSleep.config.allowDaySleeping || canSleep;
	}
}
