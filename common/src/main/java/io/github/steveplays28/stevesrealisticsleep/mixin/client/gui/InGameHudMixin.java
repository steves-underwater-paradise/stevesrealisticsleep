package io.github.steveplays28.stevesrealisticsleep.mixin.client.gui;

import io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(method = "renderSleepOverlay", at = @At(value = "HEAD"), cancellable = true)
	private void stevesrealisticsleep$cancelSleepVignetteIfDisabled(DrawContext context, RenderTickCounter tickCounter, @NotNull CallbackInfo ci) {
		if (StevesRealisticSleep.config.showSleepVignette) {
			return;
		}

		ci.cancel();
	}
}
