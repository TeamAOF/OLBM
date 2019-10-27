package io.github.alloffabric.olbm;

import io.github.alloffabric.olbm.api.LootBagType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OLBM implements ModInitializer {
	public static final String MODID = "olbm";

	public static final Logger logger = LogManager.getLogger();

	public static final Registry<LootBagType> LOOT_BAG_TYPES = new SimpleRegistry<>();

	public static final ItemGroup OLBM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "olbm_group"), () -> new ItemStack(Items.ENDER_CHEST));

	@Override
	public void onInitialize() {
		OLBData.loadConfig();
		OLBData.loadData();
	}

	public static LootBagType registerBag(LootBagType type) {
		Identifier id = type.getId();
		//TODO: container stuff
		return Registry.register(LOOT_BAG_TYPES, id, type);
	}
}
