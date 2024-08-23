package io.github.steveplays28.stevesrealisticsleep.mixin.client.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {
	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getSleepTimer()I"))
	private int stevesrealisticsleep$cancelSleepVignetteIfDisabled(ClientPlayerEntity instance, @NotNull Operation<Integer> original) {
		if (StevesRealisticSleep.config.showSleepVignette) {
			return original.call(instance);
		}

		return -1;
	}
}
