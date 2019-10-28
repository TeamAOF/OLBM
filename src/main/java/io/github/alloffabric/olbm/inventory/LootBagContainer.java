package io.github.alloffabric.olbm.inventory;

import net.minecraft.container.ContainerType;
import net.minecraft.container.GenericContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.Identifier;

public class LootBagContainer extends GenericContainer {
	private PlayerEntity player;
	private Identifier id;

	public LootBagContainer(Identifier id, int syncId, PlayerEntity player, Inventory inv) {
		super(ContainerType.GENERIC_9X3, syncId, player.inventory, inv, 3);
		this.id = id;
		this.player = player;
	}

	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		dropInventory(player, player.world, getInventory());
	}

	public Identifier getId() {
		return id;
	}

	public PlayerEntity getPlayer() {
		return player;
	}
}
