package com.github.steveplays28.realisticsleep.mixin;

import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SleepManager.class)
public class SleepManagerMixin {
	/**
	 * Cancels {@link SleepManager#canSkipNight canSkipNight} and sets the return value to <code>false</code>.
	 *
	 * @author Steveplays28
	 * @reason Method conflicts with Realistic Sleep's functionality.
	 */
	@Inject(method = "canSkipNight", at = @At(value = "HEAD"), cancellable = true)
	public void canSkipNightInject(int percentage, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}
}
