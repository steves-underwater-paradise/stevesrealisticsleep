package com.github.steveplays28.realisticsleep.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;
import static com.github.steveplays28.realisticsleep.SleepMath.WAKE_UP_TIME;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerEntityMixin {
	@Shadow
	public abstract ServerWorld getWorld();

	@Shadow
	public abstract void sendMessage(Text message, boolean overlay);

	@Inject(method = "wakeUp(ZZ)V", at = @At(value = "HEAD"))
	public void wakeUpInject(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci) {
		if (getWorld().getTimeOfDay() >= WAKE_UP_TIME) {
			// Return if we shouldn't send the dawn message
			if (!config.sendDawnMessage || config.dawnMessage.equals("")) return;

			// Send dawn HUD message to player
			sendMessage(Text.of(config.dawnMessage), true);
		}
	}
}
