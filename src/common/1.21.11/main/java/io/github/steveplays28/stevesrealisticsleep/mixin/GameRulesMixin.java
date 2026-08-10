package io.github.steveplays28.stevesrealisticsleep.mixin;

import io.github.steveplays28.stevesrealisticsleep.extension.GameRulesExtension;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRules.class)
public abstract class GameRulesMixin implements GameRulesExtension {
	@Shadow
	public abstract <T> T get(final GameRule<T> gameRule);

	@Override
	public int stevesrealisticsleep$getPlayersSleepingPercentageGameRule() {
		return this.get(GameRules.PLAYERS_SLEEPING_PERCENTAGE);
	}

	@Override
	public boolean stevesrealisticsleep$getAdvanceTimeGameRule() {
		return this.get(GameRules.ADVANCE_TIME);
	}

	@Override
	public boolean stevesrealisticsleep$getAdvanceWeatherGameRule() {
		return this.get(GameRules.ADVANCE_WEATHER);
	}
}
