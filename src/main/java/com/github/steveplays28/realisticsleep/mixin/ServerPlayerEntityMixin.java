package com.github.steveplays28.realisticsleep.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;
import static com.github.steveplays28.realisticsleep.util.SleepMathUtil.*;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity {
	public ServerPlayerEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract void sendMessage(Text message, boolean overlay);

	@Inject(method = "wakeUp(ZZ)V", at = @At(value = "HEAD"))
	public void wakeUpInject(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci) {
		var timeOfDay = getWorld().getTimeOfDay() % DAY_LENGTH;
		var ticksUntilDawn = Math.abs(timeOfDay - DAWN_WAKE_UP_TIME);
		var ticksUntilDusk = Math.abs(timeOfDay - DUSK_WAKE_UP_TIME);

		if (ticksUntilDawn > WAKE_UP_GRACE_PERIOD_TICKS && ticksUntilDusk > WAKE_UP_GRACE_PERIOD_TICKS) {
			return;
		}

		if (ticksUntilDusk < ticksUntilDawn) {
			// Return if the dusk message shouldn't be sent
			if (!config.sendDuskMessage || config.duskMessage.isEmpty()) {
				return;
			}

			// Send dusk message to player in the actionbar
			sendMessage(Text.of(config.duskMessage), true);
		} else {
			// Return if the dawn message shouldn't be sent
			if (!config.sendDawnMessage || config.dawnMessage.isEmpty()) {
				return;
			}

			// Send dawn message to player in the actionbar
			sendMessage(Text.of(config.dawnMessage), true);
		}
	}
}
