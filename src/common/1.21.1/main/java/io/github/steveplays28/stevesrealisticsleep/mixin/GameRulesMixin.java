package io.github.steveplays28.stevesrealisticsleep.mixin;

import io.github.steveplays28.stevesrealisticsleep.extension.GameRulesExtension;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRules.class)
public abstract class GameRulesMixin implements GameRulesExtension {
	@Shadow
	public abstract boolean getBoolean(GameRules.Key<GameRules.BooleanValue> key);

	@Shadow
	public abstract int getInt(GameRules.Key<GameRules.IntegerValue> key);

	@Override
	public int stevesrealisticsleep$getPlayersSleepingPercentageGameRule() {
		return this.getInt(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE);
	}

	@Override
	public boolean stevesrealisticsleep$getAdvanceTimeGameRule() {
		return this.getBoolean(GameRules.RULE_DAYLIGHT);
	}

	@Override
	public boolean stevesrealisticsleep$getAdvanceWeatherGameRule() {
		return this.getBoolean(GameRules.RULE_WEATHER_CYCLE);
	}
}
