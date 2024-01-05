# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## `v1.10.2` - 05/01/2024

### Changed

- Updated Russian translation (by [MushroomMif](https://github.com/MushroomMif) in [PR #57](https://github.com/Steveplays28/realisticsleep/pull/57))

## `v1.10.1` - 09/12/2023

### Fixed

- Crash on startup on dedicated servers due to client tick event registration code being loaded on dedicated servers
- Incorrect translation for the `worldRendererTickSpeedMultiplier`

## `v1.10.0` - 07/12/2023

### Added

- Better Clouds compatibility
  - Renamed the `cloudSpeedMultiplier` config option to `worldRendererTickSpeedMultiplier`

### Fixed

- Outdated translations in some versions of Realistic Sleep

## `v1.9.2` - 01/12/2023

### Added

- Cauldron dripstone tick speedup
  - Uses the precipitation tick speed multiplier in the config
- Serverside translation support
  - Uses the [Server Translation API](https://github.com/NucleoidMC/Server-Translations)

### Fixed

- Cauldron precipitation not taking dripstone into account
  - Cauldron precipitation now checks if dripstone can drip into the cauldron

## `v1.9.1` - 16/10/2023

### Added

- Sodium `v0.5.x` compatibility for the cloud speed multiplier

### Fixed

- Crash on startup due to the `WorldRendererMixin`

## `v1.9.0` - 15/10/2023

### Added

- Daytime sleeping (by [Superkat32](https://github.com/Superkat32) in [#48](https://github.com/Steveplays28/realisticsleep/pull/48))
- Exponential sleep speed curve
- Cloud speed multiplier
- Optimised crop growth speed multiplier implementation
- Thunder speed multiplier
- Ice and snow formation speed multiplier
- Precipitation (cauldron filling) speed multiplier
- API for use by other mods
- Separate config options for random block/fluid tick speed multipliers (disabled by default due to high TPS usage)

### Changed

- Reduced the length of the config screen tooltips (by [Superkat32](https://github.com/Superkat32)
  in [#48](https://github.com/Steveplays28/realisticsleep/pull/48))
- Config menu title to `Realistic Sleep Config` (by [Superkat32](https://github.com/Superkat32)
  in [#48](https://github.com/Steveplays28/realisticsleep/pull/48))
- HUD messages use translation entries instead of hardcoded text (split up in parts)
- Dropped Minecraft `1.18.x` support

## `v1.8.3` - 27/08/2023

### Fixed

- Day count resetting after sleeping
- Issues with certain datapacks (such as Vanilla Tweaks and Stellarity)

## `v1.8.2` - 17/08/2023

### Fixed

- A hang when loading a world or starting a server

## `v1.8.1` - 17/08/2023

### Fixed

- A crash on startup due to the `ServerPlayerEntity` mixin

## `v1.8.0` - 28/07/2023

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

## `v1.7.1` - 13/06/2023

### Added

- German (`de_de`) translation by @Cape-City in #27

### Changed

- Updated to Minecraft 1.20
