![Realistic Sleep icon](docs/media/icon_128x128.png)

# Realistic Sleep

Makes sleeping speed up time instead of skipping to day.

![Realistic Sleep gif](docs/media/realistic_sleep.gif)

## Showcase

This mod is featured in ["Minecraft Mod Combinations That Work Perfectly Together #5" By AsianHalfSquat](https://youtu.be/AMAf-oR6x5I?t=141)
[![YouTube thumbnail](https://i3.ytimg.com/vi/AMAf-oR6x5I/maxresdefault.jpg)](https://youtu.be/AMAf-oR6x5I?t=141)

## Dependencies

### Required

- [Cloth Config API](https://modrinth.com/mod/cloth-config)
- [Fabric API](https://modrinth.com/mod/fabric-api) or [Quilt Standard Libraries](https://modrinth.com/mod/qsl)

### Optional

- [ModMenu](https://modrinth.com/mod/modmenu) for an in game configuration screen (only for singleplayer/LAN)

## Incompatibilities  

Any mods that add sleep voting, or otherwise modify the sleeping mechanic itself will not work with this mod.  
You can set a minimum amount of players that have to sleep using the vanilla Minecraft gamerule `PLAYERS_SLEEP_PERCENTAGE`.

Any mods which simply add buffs/debuffs after waking up should work. [Create an issue](https://github.com/Steveplays28/realisticsleep/issues/new) on the issue tracker if you've found an incompatibility!

- BetterSleeping Revived - Buffs, debuffs, and chat messages don't trigger (see [issue #18](https://github.com/Steveplays28/realisticsleep/issues/18) for more details)
- Time and Wind - Adjusted time snaps back to vanilla time
- Time Control - Adjusted time snaps back to vanilla time

## Download

[![github](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/cozy/available/github_vector.svg)](https://github.com/Steveplays28/realisticsleep)
[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/realisticsleep)
[![curseforge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/cozy/available/curseforge_vector.svg)](https://www.curseforge.com/minecraft/mc-mods/realisticsleepfabric)

![fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/compact/supported/fabric_vector.svg)
![quilt](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/compact/supported/quilt_vector.svg)

See the version info in the filename for the supported Minecraft versions.  
Made for the Fabric and Quilt modloaders.  
Server side only.

## FAQ

![forge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@2/assets/cozy/unsupported/forge_vector.svg)

- Q: Will you be backporting this to lower Minecraft versions?  
A: No.

- Q: Forge pls?  
A: Also no.

- Q: Does this mod work in multiplayer?  
A: Yes! The speed of the night scales linearly for each player that's sleeping at the same time.

- Q: Does only the server need this mod or does the client need it too?  
A: Only the server needs this mod (but it works on the client too if you're going to host LAN or play singleplayer)

- Q: Can you still avoid rain/thunder by sleeping?  
A: Yes. After sleeping until dawn the weather will become clear. I might make this configurable in the future, let me know if you want this.

- Q: Does this mod speed up things like furnaces, redstone, the raid timer, and other (modded) block entities?  
A: Yes!

## License

This project is licensed under LGPLv2.1, see [LICENSE](https://github.com/Steveplays28/realisticsleep/blob/main/LICENSE).
