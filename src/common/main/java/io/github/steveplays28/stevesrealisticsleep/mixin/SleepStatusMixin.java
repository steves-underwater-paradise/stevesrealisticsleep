package io.github.steveplays28.stevesrealisticsleep.mixin;

import net.minecraft.server.players.SleepStatus;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SleepStatus.class)
public class SleepStatusMixin {
	/**
	 * Cancels {@link SleepStatus#areEnoughSleeping canSkipNight} and sets the return value to {@code false}.
	 * Method conflicts with Realistic Sleep's functionality.
	 *
	 * @author Steveplays28
	 */
	@Inject(method = "areEnoughSleeping", at = @At(value = "HEAD"), cancellable = true)
	public void stevesrealisticsleep$preventNightSkip(int percentage, @NotNull CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}
}
