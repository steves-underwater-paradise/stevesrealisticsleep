package io.github.steveplays28.stevesrealisticsleep.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.steveplays28.stevesrealisticsleep.StevesRealisticSleep.config;
import static io.github.steveplays28.stevesrealisticsleep.util.SleepMathUtil.*;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Entity {
	public ServerPlayerMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Shadow
	public abstract void sendSystemMessage(Component message, boolean overlay);

	@Inject(method = "stopSleepInBed(ZZ)V", at = @At(value = "HEAD"))
	public void stevesrealisticsleep$sendWakeUpMessage(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci) {
		var timeOfDay = level().getDayTime() % DAY_LENGTH;
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
			sendSystemMessage(Component.literal(config.duskMessage), true);
		} else {
			// Return if the dawn message shouldn't be sent
			if (!config.sendDawnMessage || config.dawnMessage.isEmpty()) {
				return;
			}

			// Send dawn message to player in the actionbar
			sendSystemMessage(Component.literal(config.dawnMessage), true);
		}
	}
}
