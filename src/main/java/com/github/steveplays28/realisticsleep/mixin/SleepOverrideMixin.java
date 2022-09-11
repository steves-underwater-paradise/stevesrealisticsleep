package com.github.steveplays28.realisticsleep.mixin;

import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SleepManager.class)
public class SleepOverrideMixin {
	/**
	 * @author Steveplays28
	 * @reason Method conflicts with my mod's functionality
	 */
	@Overwrite
	public boolean canSkipNight(int percentage) {
		return false;
	}
}
