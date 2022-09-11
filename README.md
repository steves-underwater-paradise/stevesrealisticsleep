<h1 align="center">
<img src="https://user-images.githubusercontent.com/62797992/164917075-5a7f8dbe-86f7-4fcb-b76f-016207d72c4c.png" width="256px" align="center">

Realistic Sleep

[![GitHub](https://img.shields.io/github/license/Steveplays28/realisticsleep)](https://github.com/Steveplays28/realisticsleep/blob/main/LICENSE)
![GitHub](https://img.shields.io/github/repo-size/Steveplays28/realisticsleep)
[![GitHub](https://img.shields.io/github/forks/Steveplays28/realisticsleep)](https://github.com/Steveplays28/realisticsleep/network/members)
[![GitHub](https://img.shields.io/github/issues/Steveplays28/realisticsleep)](https://github.com/Steveplays28/realisticsleep/issues)
[![GitHub](https://img.shields.io/github/issues-pr/Steveplays28/realisticsleep)](https://github.com/Steveplays28/realisticsleep/pulls)

![GitHub](https://img.shields.io/badge/environment-server-4caf50?style=flat-square)
![GitHub](https://img.shields.io/badge/mod%20loader-fabric-d64541?style=flat-square)
[![Discord](https://img.shields.io/discord/746681304111906867?label=chat%20on%20Discord%20%7C%20Steve%27s%20underwater%20paradise)](https://discord.gg/KbWxgGg)
</h1>

<p align="center">
Minecraft Fabric mod that makes sleeping speed up time instead of skipping to day.
</p>

## Visual demonstration  
![UI screenshot](https://github.com/Steveplays28/realisticsleep/blob/main/Minecraft%20RealisticSleep%20mod.gif)

## Download  
- [GitHub releases](https://github.com/Steveplays28/realisticsleep/releases)
- [Modrinth](https://modrinth.com/mod/realisticsleep)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/realisticsleepfabric)

## Dependencies
Client (if you're using [Mod Menu](https://modrinth.com/mod/modmenu)): [Cloth Config API](https://modrinth.com/mod/cloth-config)

## Incompatibilities  
Any mods that add sleep voting, or otherwise modify the sleeping mechanic itself will not work with this mod (sleep voting already exists by design).  
Any mods which simply add buffs/debuffs after waking up should work.

## Recommended mods  
- ModMenu for an in game configuration screen (only for singleplayer/LAN)

## FAQ  
Q: Forge pls?  
A: No. I don't have the time to learn another modding framework, however you can port over the mod yourself (LGPLv2.1) if you want, the source code is open.

Q: Will you backport this mod?  
A: No.

Q: Does this mod work in multiplayer?  
A: Yes! The speed of the night scales linearly for each player that's sleeping at the same time.

Q: Does only the server need this mod or does the client need it too?  
A: Only the server needs this mod (but it works on the client too if you're going to host LAN or play singleplayer)

Q: Can you still avoid rain/thunder by sleeping?  
A: Yes. After sleeping until dawn the weather will return to normal. I might make this configurable in the future, let me know if you want this.

Q: Does this mod speed up the block entities in loaded chunks like furnaces?  
A: Yes!
