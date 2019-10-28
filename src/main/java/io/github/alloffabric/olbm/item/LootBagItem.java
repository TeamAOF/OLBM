package io.github.alloffabric.olbm.item;

import io.github.alloffabric.olbm.OLBM;
import io.github.alloffabric.olbm.api.LootBagType;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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
		Identifier id = type.getId();
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

	@Override
	public Text getName(ItemStack stack) {
		return getName();
	}
}
