package io.github.steveplays28.stevesrealisticsleep.extension;

public interface ClientLevelExtension {
	default int stevesrealisticsleep$getPlayersSleepingPercentageGameRule() {
		throw new AssertionError("Should be implemented using a Mixin");
	}

	default boolean stevesrealisticsleep$getAdvanceTimeGameRule() {
		throw new AssertionError("Should be implemented using a Mixin");
	}

	default boolean stevesrealisticsleep$getAdvanceWeatherGameRule() {
		throw new AssertionError("Should be implemented using a Mixin");
	}
}
