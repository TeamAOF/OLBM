package io.github.alloffabric.olbm.api;

import io.github.alloffabric.olbm.item.LootBagItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class LootBagType {
	private Identifier id;
	private Identifier tableId;
	private int color;
	private boolean glint;
	private Item bag = Items.AIR;

	public LootBagType(Identifier id, Identifier tableId, int color,boolean glint, Optional<Item.Settings> settings) {
		this.id = id;
		this.tableId = tableId;
		this.color = color;
		this.glint = glint;
		settings.ifPresent(value -> this.bag = Registry.register(Registry.ITEM, id, new LootBagItem(this, value)));
	}

	public Identifier getId() {
		return id;
	}

	public Identifier getTableId() {
		return tableId;
	}

	public int getColor() {
		return color;
	}

	public boolean hasGlint() {
		return glint;
	}

	public Item getBag() {
		return bag;
	}
}
