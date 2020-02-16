package io.github.alloffabric.olbm;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import io.github.alloffabric.olbm.api.LootBagType;
import io.github.cottonmc.jankson.JanksonFactory;
import io.github.cottonmc.staticdata.StaticData;
import io.github.cottonmc.staticdata.StaticDataItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class OLBData {
	public static final Jankson jankson = JanksonFactory.createJankson();

	public static void loadConfig() {
		try {
			File file = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("olbm.json5").toFile();
			if (!file.exists()) {
				OLBM.logger.warn("[OLBM] No config file found! Generating an empty file.");
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file, false);
				out.write("{ }".getBytes());
				out.flush();
				out.close();
				return;
			}
			JsonObject json = jankson.load(file);
			loadEntries("config", json);
		} catch (IOException | SyntaxError e) {
			OLBM.logger.error("[OLBM] Error loading config: {}", e.getMessage());
		}
	}

	public static void loadData() {
		Set<StaticDataItem> data = StaticData.getAll("olbm.json5");
		for (StaticDataItem item : data) {
			try {
				JsonObject json = jankson.load(item.createInputStream());
				loadEntries(item.getIdentifier().toString(), json);
			} catch (IOException | SyntaxError e) {
				OLBM.logger.error("[OLBM] Error loading static data item {}: {}", item.getIdentifier().toString(), e.getMessage());
			}
		}
	}

	private static void loadEntries(String from, JsonObject json) {
		JsonObject bagObj = json.getObject("bags");
		if (bagObj != null) {
			List<String> keys = new ArrayList<>(bagObj.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				if (OLBM.LOOT_BAG_TYPES.hasId(new Identifier(key))) {
					OLBM.logger.error("[OLBM] Loot bag type named {} already exists, skipping it in {}", key, from);
					continue;
				}
				JsonElement elem = bagObj.get(key);
				if (elem instanceof JsonObject) {
					JsonObject config = (JsonObject) elem;
					LootBagType type = getType(key, config);
					OLBM.registerBag(type);
				}
			}
		}
	}

	//TODO: anything else needed?
	static LootBagType getType(String key, JsonObject json) {
		Identifier id = new Identifier(key);
		Identifier tableId = new Identifier(json.get(String.class, "loot"));
		int color = Integer.decode(json.get(String.class, "color").replace("#", "0x"));
		boolean makeItem = json.getBoolean("make_item", true);
		if (makeItem) {
			Rarity rarity = json.containsKey("rarity")? getRarity(json.get(String.class, "rarity")) : Rarity.COMMON;
			boolean hasGlint = json.getBoolean("glint", false);
			return new LootBagType(id, tableId, color, hasGlint, Optional.of(new Item.Settings().maxCount(1).group(OLBM.OLBM_GROUP).rarity(rarity)));
		} else {
			return new LootBagType(id, tableId, color, false, Optional.empty());
		}
	}

	static Rarity getRarity(String rarity) {
		switch(rarity.toLowerCase()) {
			case "common":
				return Rarity.COMMON;
			case "uncommon":
				return Rarity.UNCOMMON;
			case "rare":
				return Rarity.RARE;
			case "epic":
				return Rarity.EPIC;
		}
		return Rarity.COMMON;
	}
}
