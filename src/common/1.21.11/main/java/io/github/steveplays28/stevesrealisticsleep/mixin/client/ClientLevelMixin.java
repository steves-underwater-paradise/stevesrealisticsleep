package io.github.steveplays28.stevesrealisticsleep.mixin.client;

import io.github.steveplays28.stevesrealisticsleep.extension.ClientLevelExtension;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

// TODO
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin implements ClientLevelExtension {
	@Override
	public int stevesrealisticsleep$getPlayersSleepingPercentageGameRule() {
		return 0;
	}

	@Override
	public boolean stevesrealisticsleep$getAdvanceTimeGameRule() {
		return false;
	}

	@Override
	public boolean stevesrealisticsleep$getAdvanceWeatherGameRule() {
		return false;
	}
}
