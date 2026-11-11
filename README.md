# F3 Plus

![F3 Plus icon](src/main/resources/assets/f3plus/icon.png)

A small client-side mod for Minecraft 1.21 and 1.21.1 that makes the F3 debug screen easier to read.

I built it because BetterF3 was slowing my PC down. This is my own version, written from scratch, and it does only what I need: colored values, no spam from optimization mods, and a lighter background. I probably won't update it for newer Minecraft versions.

## What it changes

- values are colored, labels stay gray
- lines from optimization mods are hidden (Sodium, ImmediatelyFast, ModernFix, BadOptimizations, Iris and co.)
- a few vanilla lines nobody reads are removed
- the background is less opaque and the text has no shadow
- very long lines are cut so they can't take over the screen

Press F8 in game to open the settings. You can turn the mod off, hide the background, and choose which groups of lines to show: coordinates, chunks, entities, particles and system info. Block tags are hidden by default but can be turned back on. There is also a switch that hides mod lines. Settings are stored in `config/f3plus.json`.

## Installing

Drop the jar into your mods folder. Needs Fabric Loader 0.16 or newer. Fabric API is not required.

## Building

    ./gradlew build

The jar ends up in `build/libs`.

## License

MIT
