### Added

- Included [Server I18N API](https://modrinth.com/mod/server-i18n-api) in the JAR
- A NeoForge config button
- A config option for clear weather after sleeping

### Changed

- Marked the following mods as incompatible:
  - sleepwarp
  - sleeping-overhaul-2
  - fast-sleep
  - seamless-sleep
  - better-sleep
  - vanilla-outsider-true-sleep
  - sleep-timelapse

### Fixed

- Ticks per second being hard-coded
  - Added ticks per second estimation (thank you to [mg95](https://github.com/themg95) in [#76](https://github.com/steves-underwater-paradise/stevesrealisticsleep/pull/76))
- `PLAYERS_SLEEPING_PERCENTAGE` gamerule being set to 0 causing sleeping tick speed modifiers to be applied (thank you to [Vercte](https://github.com/vercte) in [#93](https://github.com/steves-underwater-paradise/stevesrealisticsleep/pull/93))
