package io.github.steveplays28.stevesrealisticsleep.mixin.accessor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.SleepStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerLevel.class)
public interface ServerWorldAccessor {
	@Accessor("sleepStatus")
	SleepStatus getSleepStatus();
}
