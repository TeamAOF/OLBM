package io.github.alloffabric.olbm.item;

import io.github.alloffabric.olbm.api.LootBagType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.loot.LootSupplier;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextParameters;
import net.minecraft.world.loot.context.LootContextTypes;

import java.util.List;

public class LootBagItem extends Item {
	private LootBagType type;

	public LootBagItem(LootBagType type, Settings settings) {
		super(settings);
		this.type = type;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if (!world.isClient) {
			ServerWorld server = (ServerWorld)world;
			Identifier id = type.getTableId();
			LootContext ctx = new LootContext.Builder(server).put(LootContextParameters.THIS_ENTITY, player).put(LootContextParameters.POSITION, player.getBlockPos()).build(LootContextTypes.GIFT);
			LootSupplier supplier = server.getServer().getLootManager().getSupplier(id);
			List<ItemStack> stacks = supplier.getDrops(ctx);
			//TODO: make a gui instead of just spitting them everywhere
			for (ItemStack stack : stacks) {
				player.dropItem(stack, false);
			}
			if (!player.isCreative()) player.getStackInHand(hand).decrement(1);
			return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
		}
		return new TypedActionResult<>(ActionResult.FAIL, player.getStackInHand(hand));
	}
}
