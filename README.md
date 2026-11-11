# F3 Plus

![F3 Plus icon](src/main/resources/assets/f3plus/icon.png)

A small client-side mod for Minecraft 1.21 and 1.21.1 that makes the F3 debug screen easier to read.

I built it because BetterF3 was slowing my PC down. This is my own version, written from scratch, and it does only what I need: colored values, no spam from optimization mods, and no dark panel over the world. I probably won't update it for newer Minecraft versions.

## What it changes

- values are colored, labels stay gray
- lines from optimization mods are hidden (Sodium, ImmediatelyFast, ModernFix, BadOptimizations, Iris and co.)
- a few vanilla lines nobody reads are removed
- the background is the normal Minecraft one and can be turned off
- the text has no shadow
- very long lines are cut so they can't take over the screen

Press F8 in game to open the settings. You can turn the mod off, hide the background, and choose which groups of lines to show: coordinates, chunks, entities, particles and system info. Block tags are hidden by default but can be turned back on. There is also a switch that hides mod lines. Settings are stored in `config/f3plus.json`.

The settings key is a normal key binding, so you can change it in Options, Controls. If Mod Menu is installed, the settings are also on the mod's page there.

One thing that can look like a bug: the vanilla Vignette effect darkens the screen edges and moves with the camera. If you see a dark shape that follows your view, that is Vignette. Turn it off in Video Settings if you do not like it.

## Installing

Drop the jar into your mods folder. Needs Fabric Loader 0.16 or newer. Fabric API is not required.

## Building

    ./gradlew build

The jar ends up in `build/libs`.

## License

MIT
