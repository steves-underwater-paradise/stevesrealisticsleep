package io.github.steveplays28.stevesrealisticsleep.mixin.accessor;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerWorld.class)
public interface ServerWorldAccessor {
	@Accessor("sleepManager")
	SleepManager getSleepManager();
}
