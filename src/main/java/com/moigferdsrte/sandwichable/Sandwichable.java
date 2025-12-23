package com.moigferdsrte.sandwichable;

import com.moigferdsrte.sandwichable.advancement.CollectSandwichCriterion;
import com.moigferdsrte.sandwichable.advancement.CutItemCriterion;
import com.moigferdsrte.sandwichable.advancement.ToastItemCriterion;
import com.moigferdsrte.sandwichable.advancement.UseBottleCrateCriterion;
import com.moigferdsrte.sandwichable.blocks.entity.BottleCrateBlockEntity;
import com.moigferdsrte.sandwichable.blocks.entity.DesalinatorBlockEntity;
import com.moigferdsrte.sandwichable.blocks.entity.screen.BottleCrateScreenHandler;
import com.moigferdsrte.sandwichable.blocks.entity.screen.DesalinatorScreenHandler;
import com.moigferdsrte.sandwichable.blocks.entity.screen.data.BottleCratePos;
import com.moigferdsrte.sandwichable.blocks.entity.screen.data.DesalinatorPos;
import com.moigferdsrte.sandwichable.blocks.loot.CopyWorldBiomeLootFunction;
import com.moigferdsrte.sandwichable.common.CommonTags;
import com.moigferdsrte.sandwichable.components.SandwichableDataComponent;
import com.moigferdsrte.sandwichable.config.SandwichableConfig;
import com.moigferdsrte.sandwichable.fluids.FluidsRegistry;
import com.moigferdsrte.sandwichable.items.KitchenKnifeItem;
import com.moigferdsrte.sandwichable.items.extra.ItemGroupQueue;
import com.moigferdsrte.sandwichable.items.extra.SandwichableGroupIconBuilder;
import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import com.moigferdsrte.sandwichable.registry.NetworkRegistry;
import com.moigferdsrte.sandwichable.registry.RecipeRegistry;
import com.moigferdsrte.sandwichable.util.AncientGrainType;
import com.moigferdsrte.sandwichable.util.ExtraDispenserBehaviorRegistry;
import com.moigferdsrte.sandwichable.util.SpreadRegistry;
import com.moigferdsrte.sandwichable.util.Util;
import com.moigferdsrte.sandwichable.villager.SandwichMakerProfession;
import com.moigferdsrte.sandwichable.worldgen.SandwichableWorldgen;
import com.moigferdsrte.sandwichable.worldgen.VillagerHouses;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sandwichable implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(Util.MOD_ID);
	public static final ItemGroupQueue SANDWICHABLE_ITEMS = new ItemGroupQueue(Util.id("sandwichable"));
	public static final ItemGroup SANDWICHABLE_GROUP = FabricItemGroup.builder()
			.icon(SandwichableGroupIconBuilder::getIcon)
			.displayName(Text.translatable(net.minecraft.util.Util.createTranslationKey("itemGroup", SANDWICHABLE_ITEMS.id)))
			.entries(SANDWICHABLE_ITEMS)
			.build();

	public static final ExtendedScreenHandlerType<DesalinatorScreenHandler, DesalinatorPos> DESALINATOR_HANDLER = new ExtendedScreenHandlerType<>((syncId, playerInv, pos) -> {
		BlockEntity be = playerInv.player.getEntityWorld().getBlockEntity(pos.pos());
		if(be instanceof DesalinatorBlockEntity s) return new DesalinatorScreenHandler(syncId, playerInv, s);
		return null;
	}, DesalinatorPos.PACKET_CODEC);

	public static final ExtendedScreenHandlerType<BottleCrateScreenHandler, BottleCratePos> BOTTLE_CRATE_HANDLER = new ExtendedScreenHandlerType<>((syncId, playerInv, pos) -> {
		BlockEntity be = playerInv.player.getEntityWorld().getBlockEntity(pos.pos());
		if(be instanceof BottleCrateBlockEntity s) return new BottleCrateScreenHandler(syncId, playerInv, s);
		return null;
	}, BottleCratePos.PACKET_CODEC);

	public static final TagKey<Item> BREAD_SLICES = TagKey.of(RegistryKeys.ITEM, Util.id("bread_slices"));
	public static final TagKey<Item> BREAD_LOAVES = TagKey.of(RegistryKeys.ITEM, Util.id("bread_loaves"));
	public static final TagKey<Item> METAL_ITEMS = TagKey.of(RegistryKeys.ITEM, Util.id("metal_items"));
	public static final TagKey<Item> SMALL_FOODS = TagKey.of(RegistryKeys.ITEM, Util.id("small_foods"));
	public static final TagKey<Item> CUTTING_BOARDS = TagKey.of(RegistryKeys.ITEM, Util.id("cutting_boards"));
	public static final TagKey<Item> CHEESE_WHEELS = TagKey.of(RegistryKeys.ITEM, Util.id("cheese_wheels"));
	public static final TagKey<Block> SALT_PRODUCING_BLOCKS = TagKey.of(RegistryKeys.BLOCK, Util.id("salt_producing_blocks"));
	public static final TagKey<Block> KNIFE_SHARPENING_SURFACES = TagKey.of(RegistryKeys.BLOCK, Util.id("knife_sharpening_surfaces"));

	public static final TagKey<Biome> SALT_WATER_BODIES = TagKey.of(RegistryKeys.BIOME, Util.id("salt_water_bodies"));
	public static final TagKey<Biome> NO_SHRUBS = TagKey.of(RegistryKeys.BIOME, Util.id("no_shrubs"));
	public static final TagKey<Biome> NO_SALT_POOLS = TagKey.of(RegistryKeys.BIOME, Util.id("no_salt_pools"));

	public static final GameRules.Key<GameRules.IntRule> SANDWICH_SIZE_RULE = GameRuleRegistry.register("maxSandwichSize", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(-1, -1));
	public static final GameRules.Key<GameRules.BooleanRule> PICKLE_BRINE_SOURCE_CONVERSION_RULE = GameRuleRegistry.register("pickleBrineSourceConversion", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(false));

	public static final SoundEvent DESALINATOR_START = Registry.register(Registries.SOUND_EVENT, Util.id("desalinator_start"), SoundEvent.of(Util.id("desalinator_start")));
	public static final SoundEvent DESALINATOR_RUN = Registry.register(Registries.SOUND_EVENT, Util.id("desalinator_run"), SoundEvent.of(Util.id("desalinator_run")));
	public static final SoundEvent DESALINATOR_STOP = Registry.register(Registries.SOUND_EVENT, Util.id("desalinator_stop"), SoundEvent.of(Util.id("desalinator_stop")));

	public static final LootFunctionType<CopyWorldBiomeLootFunction> COPY_WORLD_BIOME = Registry.register(Registries.LOOT_FUNCTION_TYPE, Util.id("copy_world_biome"), new LootFunctionType<>(CopyWorldBiomeLootFunction.CODEC));
	private static final Identifier ANCIENT_CITY_LOOT = Identifier.ofVanilla("chests/ancient_city");

	public static final CutItemCriterion CUT_ITEM = Registry.register(Registries.CRITERION, Util.id("cut_item"), new CutItemCriterion());
	public static final ToastItemCriterion TOAST_ITEM = Registry.register(Registries.CRITERION, Util.id("toast_item"), new ToastItemCriterion());
	public static final UseBottleCrateCriterion USE_BOTTLE_CRATE = Registry.register(Registries.CRITERION, Util.id("use_bottle_crate"), new UseBottleCrateCriterion());
	public static final CollectSandwichCriterion COLLECT_SANDWICH = Registry.register(Registries.CRITERION, Util.id("collect_sandwich"), new CollectSandwichCriterion());

	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM_GROUP, SANDWICHABLE_ITEMS.id, SANDWICHABLE_GROUP);

		Util.getConfig();
		BlocksRegistry.init();
		ItemsRegistry.init();
		FluidsRegistry.init();
		SpreadRegistry.init();
		RecipeRegistry.init();
		SandwichableWorldgen.init();
		SandwichableDataComponent.init();
		CommonTags.init();
		SandwichMakerProfession.init();
		NetworkRegistry.registerPayload();
		NetworkRegistry.serverInit();
		VillagerHouses.register();
		AncientGrainType.init();

		ExtraDispenserBehaviorRegistry.initDefaults();

		Registry.register(Registries.SCREEN_HANDLER, Util.id("desalinator_handler"), DESALINATOR_HANDLER);
		Registry.register(Registries.SCREEN_HANDLER, Util.id("bottle_crate_handler"), BOTTLE_CRATE_HANDLER);

		DispenserBehavior defaultBehavior = new ItemDispenserBehavior();
		DispenserBlock.registerBehavior(ItemsRegistry.PICKLE_BRINE_BUCKET, (pointer, stack) -> {
			BlockPos pos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
			BucketItem bucket = (BucketItem)stack.getItem();
			World world = pointer.world();
			if (bucket.placeFluid(null, world, pos, null)) {
				bucket.onEmptied(null, world, stack, pos);
				return new ItemStack(Items.BUCKET);
			} else {
				return defaultBehavior.dispense(pointer, stack);
			}
		});

		UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
			BlockPos pos = hit.getBlockPos();
			if (world.getBlockState(pos).isIn(KNIFE_SHARPENING_SURFACES)) {
				ItemStack knife = player.getStackInHand(hand);
				SandwichableConfig.KitchenKnifeOption opt = Util.getConfig().getKnifeOption(knife.getItem());
				if (opt != null && KitchenKnifeItem.getSharpnessF(knife) < 1) {
					Vec3d hPos = hit.getPos();
					if (world.isClient()) {
						for (int i = 0; i < 4; i++) {
							world.addParticle(ParticleTypes.CRIT, hPos.x, hPos.y, hPos.z, (world.random.nextFloat() - 0.5) * 0.5, 0.1, (world.random.nextFloat() - 0.5) * 0.5);
						}
						return ActionResult.SUCCESS;
					}
					KitchenKnifeItem.setSharpness(knife, KitchenKnifeItem.getSharpness(knife) + 3);
					world.playSound(null, hPos.x, hPos.y, hPos.z, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 0.7f, 1.5f + (world.random.nextFloat() * 0.2f));
					return ActionResult.CONSUME;
				}
			}
			return ActionResult.PASS;
		});

		LootTableEvents.MODIFY.register((resources, builder, source, wrapperLookup) -> {
			if (source.isBuiltin() && ANCIENT_CITY_LOOT.equals(resources.getRegistry())) {
				builder.pool(LootPool.builder().with(
								ItemEntry.builder(ItemsRegistry.ANCIENT_GRAIN_SEEDS)
										.apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3)))
						).rolls(UniformLootNumberProvider.create(0, 2))
				);
			}
		});
	}

	public static boolean isBread(ItemStack stack) {
		return stack.isIn(BREAD_SLICES) || stack.isIn(BREAD_LOAVES);
	}
}