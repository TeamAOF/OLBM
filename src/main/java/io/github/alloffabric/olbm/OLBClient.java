package io.github.alloffabric.olbm;

import com.swordglowsblue.artifice.api.Artifice;
import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder;
import com.swordglowsblue.artifice.api.util.Processor;
import io.github.alloffabric.olbm.api.LootBagType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class OLBClient implements ClientModInitializer {

	public static final Map<Identifier, Processor<ModelBuilder>> MODELS = new HashMap<>();
	@Override
	public void onInitializeClient() {
		createModels();
		Artifice.registerAssets(new Identifier(OLBM.MODID, "olbm_assets"), assets -> {
			for (Identifier id : MODELS.keySet()) {
				assets.addItemModel(id, MODELS.get(id));
			}
		});
		for (Identifier id : OLBM.LOOT_BAG_TYPES.getIds()) {
			LootBagType type = OLBM.LOOT_BAG_TYPES.get(id);
			ColorProviderRegistry.ITEM.register((stack, layer) -> type.getColor(), type.getBag());
		}
	}

	public static void createModels() {
		for (Identifier id : OLBM.LOOT_BAG_TYPES.getIds()) {
			LootBagType type = OLBM.LOOT_BAG_TYPES.get(id);
			if (type.getBag() != Items.AIR) {
				MODELS.put(id, (builder) ->
						builder.parent(new Identifier("item/generated"))
								.texture("layer0", new Identifier(OLBM.MODID, "item/bag")));
			}
		}
	}
}
