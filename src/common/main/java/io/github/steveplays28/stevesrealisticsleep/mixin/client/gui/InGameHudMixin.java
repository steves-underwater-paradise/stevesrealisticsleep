package io.github.steveplays28.stevesrealisticsleep.mixin.client.gui;

import io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {
	@Inject(method = "renderSleepOverlay", at = @At(value = "HEAD"), cancellable = true)
	private void stevesrealisticsleep$cancelSleepVignetteIfDisabled(GuiGraphics context, DeltaTracker tickCounter, @NotNull CallbackInfo ci) {
		if (StevesRealisticSleep.config.showSleepVignette) {
			return;
		}

		ci.cancel();
	}
}
