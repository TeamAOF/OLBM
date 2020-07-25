package io.github.alloffabric.olbm.item;

import blue.endless.jankson.annotation.Nullable;
import io.github.alloffabric.olbm.OLBClient;
import io.github.alloffabric.olbm.OLBM;
import io.github.alloffabric.olbm.api.LootBagType;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.world.World;

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
			LootContext ctx = new LootContext.Builder(server).parameter(LootContextParameters.THIS_ENTITY, player).parameter(LootContextParameters.POSITION, player.getBlockPos()).build(LootContextTypes.GIFT);
			LootTable supplier = server.getServer().getLootManager().getTable(id);
			List<ItemStack> stacks = supplier.generateLoot(ctx);
			ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(OLBM.MODID, "loot_bag"), player, buf -> {
				buf.writeIdentifier(type.getId());
				buf.writeVarInt(stacks.size());
				for (ItemStack stack : stacks) {
					buf.writeItemStack(stack);
				}
			});
			if (!player.isCreative()) player.getStackInHand(hand).decrement(1);
			return new TypedActionResult<>(ActionResult.SUCCESS, player.getStackInHand(hand));
		}
		return new TypedActionResult<>(ActionResult.FAIL, player.getStackInHand(hand));
	}

	@Override
	public Text getName() {
		return OLBClient.getName(type.getId());
	}

	@Override
	public Text getName(ItemStack stack) {
		return getName();
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return type.hasGlint();
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		if (context.isAdvanced()) tooltip.add(new TranslatableText("tooltip.olbm.source").formatted(Formatting.BLUE, Formatting.ITALIC));
	}
}
