# LagGoggles Legacy

![LagGoggles Legacy Logo](docs/logo.png)

A backport of [TerminatorNL](https://github.com/TerminatorNL)'s LagGoggles to 1.7.10.

## Dependencies
- [FalsePatternLib](https://github.com/FalsePattern/FalsePatternLib)
- [UniMixins](https://github.com/FalsePattern/UniMixins)

## Original description:

Intuitive lag hunting!

LagGoggles is a profiler where you can see timings in the world, visually. It comes with a custom GUI using a
configurable keybind.

You can use LagGoggles as a player too! This is my attempt to create an understanding of what causes lag on a server to regular players.

I noticed some common misconceptions like having machines spread out over different chunks causes less lag than putting it in one chunk.

With LagGoggles, you can look around and see that alot of small things like pipes or cables cause a tiny bit of lag most of the time, often resulting in more lag than a machine right next to the source would.

## Screenshots

| LagGoggles enabled | normal view |
| --- | --- |
| ![alt text](docs/2-enabled.png "With laggoggles") | ![alt text](docs/2-disabled.png "Without laggoggles") |
| ![alt text](docs/1-enabled.png "With laggoggles") | ![alt text](docs/1-disabled.png "Without laggoggles") |
| ![alt text](docs/3-enabled.png "With laggoggles") | ![alt text](docs/3-disabled.png "Without laggoggles") |

![alt text](docs/menu.png "LagGoggles menu")
![alt text](docs/menu-5sec.png "LagGoggles menu")
![alt text](docs/sheep.png "Entity view")
![alt text](docs/timingsmenu.png "Analyzer")

## Video explaining the concept
Massive thanks to [Grok DuckFarmer](https://www.youtube.com/channel/UCoKMLbTK35pzF8Lv_oj3KlA) for making this video.


[Minecraft Talk 35 - LagGoggles and Better Server Play](https://youtu.be/2ALrIvw1S4I)

[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/2ALrIvw1S4I/0.jpg)](http://www.youtube.com/watch?v=2ALrIvw1S4I)

## Features
* Teleporting to sources of lag
* Client GUI, including overlay and clickable menus
* Colour scale for easy spotting of sources of lag.
* Calculations are done on the client, so each client can have a different colour scale.
* Clientside FPS support!
   * Checks render time for Entities, Tile entities and event subscribers
   * It's very young, but it should give some insight in what may cause FPS issues.
* Config for client:
    * Custom gradients
    * Colorblindess support
* Config for server
    * Custom permissions for players (non-operators)

## Configuration
* Drop the jar in the /mods folder
* Serverside configuration is not required and has no effect.
* Clients can edit the config to change the colour scale.

## How it works
Entities, blocks and event-subscribers are timed using System.nanotime() before and after the tick() and update() methods. These times are being recorded and sent to the client when the profiler is done.

## Benefits
* Visualisation of various sources of lag
* Learn your players to build lag-free bases.
