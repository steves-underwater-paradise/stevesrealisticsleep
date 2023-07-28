### Added

- More configurability (by [RickyTheRacc](https://github.com/RickyTheRacc) in [#31](https://github.com/Steveplays28/realisticsleep/pull/31))
  - `sendDawnMessage`: makes it more obvious that the dawn message can be disabled
  - `showTimeUntilDawn`: allows disabling the time until dawn in the sleep message, to allow for greater vanilla parity
- Russian translation (by [1-nkl](https://github.com/1-nkl) in [#36](https://github.com/Steveplays28/realisticsleep/pull/36))
- Compatibility with Comforts' Hammocks
  - Daytime and nighttime is differentiated between in the HUD messages

### Fixed

- Players get kicked out of bed slightly earlier than they should
- Amount of players needed to sleep message shows 0/X instead of the actual (non-zero) value
- After sleeping, the next weather change will always be a thunderstorm
