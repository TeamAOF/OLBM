package io.github.alloffabric.olbm;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.impl.SyntaxError;
import io.github.alloffabric.olbm.api.LootBagType;
import io.github.cottonmc.jankson.JanksonFactory;
import io.github.cottonmc.staticdata.StaticData;
import io.github.cottonmc.staticdata.StaticDataItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

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
		Set<StaticDataItem> data = StaticData.getAll("artis.json5");
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
		List<String> keys = new ArrayList<>(json.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			if (OLBM.LOOT_BAG_TYPES.containsId(new Identifier(key))) {
				OLBM.logger.error("[OLBM] Table type named {} already exists, skipping it in {}", key, from);
				continue;
			}
			JsonElement elem = json.get(key);
			if (elem instanceof JsonObject) {
				JsonObject config = (JsonObject)elem;
				LootBagType type = getType(key, config);
				OLBM.registerBag(type);
			}
		}
	}

	//TODO: anything else needed?
	static LootBagType getType(String key, JsonObject json) {
		Identifier id = new Identifier(key);
		Identifier tableId = new Identifier(json.get(String.class, "loot"));
		int color = Integer.decode(json.get(String.class, "color").replace("#", "0x"));
		boolean makeItem = true;
		if (json.containsKey("has_item")) {
			makeItem = json.get(Boolean.class, "has_item");
		}
		if (makeItem) {
			return new LootBagType(id, tableId, color, Optional.of(new Item.Settings().maxCount(1).group(OLBM.OLBM_GROUP)));
		} else {
			return new LootBagType(id, tableId, color, Optional.empty());
		}
	}
}
