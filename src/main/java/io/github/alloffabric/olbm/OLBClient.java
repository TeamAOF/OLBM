package io.github.alloffabric.olbm;

import com.swordglowsblue.artifice.api.Artifice;
import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder;
import com.swordglowsblue.artifice.api.util.Processor;
import io.github.alloffabric.olbm.api.LootBagType;
import io.github.alloffabric.olbm.inventory.LootBagContainer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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
		ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier(OLBM.MODID, "loot_bag"), container -> new GenericContainerScreen((LootBagContainer)container, ((LootBagContainer)container).getPlayer().inventory, getName(((LootBagContainer)container).getId())));
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

	public static Text getName(Identifier id) {
		String key = "lootbag." + id.getNamespace() + "." + id.getPath();
		if (I18n.hasTranslation(key)) return new TranslatableText(key);
		else {
			StringBuilder builder = new StringBuilder();
			//TODO: do we want the namespace here?
//			builder.append(id.getNamespace().substring(0, 1).toUpperCase());
//			builder.append(id.getNamespace().substring(1) + " ");
			String[] splitPath = id.getPath().split("_");
			for (String substr : splitPath) {
				builder.append(substr.substring(0, 1).toUpperCase());
				builder.append(substr.substring(1));
				builder.append(" ");
			}
			return new LiteralText(builder.toString());
		}
	}
}
