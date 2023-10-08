package com.github.steveplays28.realisticsleep.mixin;

import com.github.steveplays28.realisticsleep.util.SleepMathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.github.steveplays28.realisticsleep.RealisticSleep.config;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends Entity {
	public ServerPlayerEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract void sendMessage(Text message, boolean overlay);

	@Shadow public abstract ServerWorld getWorld();

	@Inject(method = "wakeUp(ZZ)V", at = @At(value = "HEAD"))
	public void wakeUpInject(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci) {
		if (!SleepMathUtil.isNightTime(getWorld().getTimeOfDay())) {
			// Return if we shouldn't send the dawn message
			if (!config.sendDawnMessage || config.dawnMessage.isEmpty()) return;

			// Send dawn HUD message to player
			sendMessage(Text.of(config.dawnMessage), true);
		} else if (config.allowDaySleeping && SleepMathUtil.isNightTime(getWorld().getTimeOfDay())) { //Only shows this message if day sleeping is allowed
			// Return if we shouldn't send the dawn or dusk message
			if (!config.sendDuskMessage || config.duskMessage.isEmpty()) return;

			// Send dawn or dusk HUD message to player
			sendMessage(Text.of(config.duskMessage), true);
		}
	}
}
