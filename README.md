<img src="icon.png" align="right" width="180px"/>

# Obligatory Loot Bag Mod


[>> Downloads <<](https://github.com/AllOfFabric/OLBM/releases)

*Get set up!*

**This mod is open source and under a permissive license.** As such, it can be included in any modpack on any platform without prior permission. We appreciate hearing about people using our mods, but you do not need to ask to use them. See the [LICENSE file](LICENSE) for more details.

OLBM adds loot bags created through static data and driven by loot tables for pack devs to add into their modpacks.

## JSON format

Loot bags are defined in `.minecraft/config/olbm.json5` file, or in any static data directory as `olbm.json5`. A typical file should look like this:
```JSON
{
  "bags": {
    "olbm:test_bag": {
      "loot": "minecraft:chests/simple_dungeon",
      "color": "#FF7253",
      "rarity": "epic",
      "glint": true
    },
  }
}
```
The JSON should have one primary object, named `bags`, which all bags are defined in. Each bag should be an object, and its in-game ID will be defined by its key in the `bags` object.

Each bag configuration can store the following properties:
- `"loot"` - The loot table this bag will roll from when opened. Required
- `"color"` - The color of the bag item. Required.
- `"rarity"` - The rarity of the bag item (determines the item's name color). Optional; defaults to `"common"`.
- `"glint"` - Whether the item should have an enchantment glint. Optional; defaults to `false`.
- `"make_item"` - Whether an item should be created by OLBM for this loot bag. Optional; defaults to `true`.
