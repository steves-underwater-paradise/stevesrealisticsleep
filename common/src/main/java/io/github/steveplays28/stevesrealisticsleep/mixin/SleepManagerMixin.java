package io.github.steveplays28.stevesrealisticsleep.mixin;

import net.minecraft.server.world.SleepManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SleepManager.class)
public class SleepManagerMixin {
	/**
	 * Cancels {@link SleepManager#canSkipNight canSkipNight} and sets the return value to {@code false}.
	 *
	 * @author Steveplays28
	 * @reason Method conflicts with Realistic Sleep's functionality.
	 */
	@Inject(method = "canSkipNight", at = @At(value = "HEAD"), cancellable = true)
	public void stevesrealisticsleep$preventNightSkip(int percentage, @NotNull CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}
}
