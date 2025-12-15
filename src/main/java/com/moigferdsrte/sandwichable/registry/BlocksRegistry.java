package com.moigferdsrte.sandwichable.registry;

import com.moigferdsrte.sandwichable.Sandwichable;
import com.moigferdsrte.sandwichable.blocks.*;
import com.moigferdsrte.sandwichable.blocks.entity.*;
import com.moigferdsrte.sandwichable.fluids.FluidsRegistry;
import com.moigferdsrte.sandwichable.items.InfoTooltipBlockItem;
import com.moigferdsrte.sandwichable.items.extra.ItemGroupQueue;
import com.moigferdsrte.sandwichable.util.Util;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ColorCode;

public class BlocksRegistry {

    public static final Block SALTY_AIR = new SaltyAirBlock(AbstractBlock.Settings.copy(Blocks.CAVE_AIR));
    public static final Block SANDWICH_TABLE = new SandwichTableBlock(AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE));
    public static final Block SANDWICH = new SandwichBlock(AbstractBlock.Settings.copy(Blocks.CAKE).nonOpaque().breakInstantly());
    public static final Block OAK_CUTTING_BOARD = new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.OAK_PRESSURE_PLATE));
    public static final Block BIRCH_CUTTING_BOARD = new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.BIRCH_PRESSURE_PLATE));
    public static final Block SPRUCE_CUTTING_BOARD = new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.SPRUCE_PRESSURE_PLATE));
    public static final Block JUNGLE_CUTTING_BOARD = new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.JUNGLE_PRESSURE_PLATE));
    public static final Block ACACIA_CUTTING_BOARD = new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.ACACIA_PRESSURE_PLATE));
    public static final Block DARK_OAK_CUTTING_BOARD = new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.DARK_OAK_PRESSURE_PLATE));
    public static final Block MANGROVE_CUTTING_BOARD = new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.MANGROVE_PRESSURE_PLATE));
    public static final Block CHERRY_CUTTING_BOARD = new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.CHERRY_PRESSURE_PLATE));
    public static final Block BAMBOO_CUTTING_BOARD = new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.BAMBOO_PRESSURE_PLATE));
    public static final Block CRIMSON_CUTTING_BOARD = new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.CRIMSON_PRESSURE_PLATE));
    public static final Block WARPED_CUTTING_BOARD = new CuttingBoardBlock(AbstractBlock.Settings.copy(Blocks.WARPED_PRESSURE_PLATE));
    public static final Block LETTUCE = new LettuceCropBlock(AbstractBlock.Settings.copy(Blocks.WHEAT));
    public static final Block TOMATOES = new TomatoCropBlock(AbstractBlock.Settings.copy(Blocks.WHEAT));
    public static final Block ONIONS = new OnionCropBlock(AbstractBlock.Settings.copy(Blocks.WHEAT));
    public static final Block CUCUMBERS = new CucumberCropBlock(AbstractBlock.Settings.copy(Blocks.WHEAT));
    public static final Block ANCIENT_GRAIN = new AncientGrainBlock(AbstractBlock.Settings.copy(Blocks.WHEAT).offset(AbstractBlock.OffsetType.XZ));
    public static final Block PICKLE_BRINE = new PickleBrineFluidBlock(FluidsRegistry.PICKLE_BRINE, AbstractBlock.Settings.copy(Blocks.WATER));
    public static final Block PICKLE_JAR = new PickleJarBlock(AbstractBlock.Settings.copy(Blocks.GLASS_PANE));
    public static final Block SALTY_SAND = new ColoredFallingBlock(new ColorCode(14406560), AbstractBlock.Settings.copy(Blocks.SAND));
    public static final Block SALTY_STONE = new Block(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).strength(1.7f, 6.5f).sounds(BlockSoundGroup.STONE));
    public static final Block SALTY_ROCKS = new Block(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).strength(1.7f, 6.5f).sounds(BlockSoundGroup.STONE));
    public static final Block SHRUB = new ShrubBlock(AbstractBlock.Settings.copy(Blocks.DEAD_BUSH));
    public static final Block POTTED_SHRUB = new PottedShrubBlock(SHRUB, AbstractBlock.Settings.copy(Blocks.POTTED_DEAD_BUSH));
    public static final Block TOASTER = new ToasterBlock(AbstractBlock.Settings.copy(Blocks.STONECUTTER));
    public static final Block ANDESITE_BASIN = new BasinBlock(AbstractBlock.Settings.copy(Blocks.ANDESITE));
    public static final Block DIORITE_BASIN = new BasinBlock(AbstractBlock.Settings.copy(Blocks.DIORITE));
    public static final Block GRANITE_BASIN = new BasinBlock(AbstractBlock.Settings.copy(Blocks.GRANITE));
    public static final Block BASALT_BASIN = new BasinBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_BASALT));
    public static final Block BLACKSTONE_BASIN = new BasinBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_BLACKSTONE));
    public static final Block DEEPSLATE_BASIN = new BasinBlock(AbstractBlock.Settings.copy(Blocks.POLISHED_DEEPSLATE));
    public static final Block COPPER_BASIN = new OxidizableBasinBlock(Oxidizable.OxidationLevel.UNAFFECTED, AbstractBlock.Settings.copy(Blocks.CUT_COPPER));
    public static final Block EXPOSED_COPPER_BASIN = new OxidizableBasinBlock(Oxidizable.OxidationLevel.EXPOSED, AbstractBlock.Settings.copy(Blocks.EXPOSED_CUT_COPPER));
    public static final Block WEATHERED_COPPER_BASIN = new OxidizableBasinBlock(Oxidizable.OxidationLevel.WEATHERED, AbstractBlock.Settings.copy(Blocks.WEATHERED_CUT_COPPER));
    public static final Block OXIDIZED_COPPER_BASIN = new OxidizableBasinBlock(Oxidizable.OxidationLevel.OXIDIZED, AbstractBlock.Settings.copy(Blocks.OXIDIZED_CUT_COPPER));
    public static final Block WAXED_COPPER_BASIN = new BasinBlock(AbstractBlock.Settings.copy(Blocks.WAXED_CUT_COPPER));
    public static final Block WAXED_EXPOSED_COPPER_BASIN = new BasinBlock(AbstractBlock.Settings.copy(Blocks.WAXED_EXPOSED_CUT_COPPER));
    public static final Block WAXED_WEATHERED_COPPER_BASIN = new BasinBlock(AbstractBlock.Settings.copy(Blocks.WAXED_WEATHERED_CUT_COPPER));
    public static final Block WAXED_OXIDIZED_COPPER_BASIN = new BasinBlock(AbstractBlock.Settings.copy(Blocks.WAXED_OXIDIZED_CUT_COPPER));
    public static final Block DESALINATOR = new DesalinatorBlock(AbstractBlock.Settings.copy(Blocks.STONECUTTER).luminance(state -> state.get(DesalinatorBlock.ON) ? 13 : 4));
    public static final Block BOTTLE_CRATE = new BottleCrateBlock(AbstractBlock.Settings.copy(Blocks.BARREL));


    public static BlockEntityType<DesalinatorBlockEntity> DESALINATOR_BLOCKENTITY;
    public static BlockEntityType<CuttingBoardBlockEntity> CUTTINGBOARD_BLOCKENTITY;
    public static BlockEntityType<BasinBlockEntity> BASIN_BLOCKENTITY;
    public static BlockEntityType<ToasterBlockEntity> TOASTER_BLOCKENTITY;
    public static BlockEntityType<SandwichTableBlockEntity> SANDWICHTABLE_BLOCKENTITY;
    public static BlockEntityType<SandwichBlockEntity> SANDWICH_BLOCKENTITY;
    public static BlockEntityType<PickleJarBlockEntity> PICKLEJAR_BLOCKENTITY;
    public static BlockEntityType<BottleCrateBlockEntity> BOTTLECRATE_BLOCKENTITY;

    //===============================================================================
    public static void init() {
        registerBlock(SALTY_AIR, "salty_air");

        registerBlock(SANDWICH_TABLE, "sandwich_table", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(SANDWICH, "sandwich");

        registerBlock(TOASTER, "toaster", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(ANDESITE_BASIN, "andesite_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(DIORITE_BASIN, "diorite_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(GRANITE_BASIN, "granite_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(BASALT_BASIN, "basalt_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(BLACKSTONE_BASIN, "blackstone_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(DEEPSLATE_BASIN, "deepslate_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(COPPER_BASIN, "copper_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(EXPOSED_COPPER_BASIN, "exposed_copper_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(WEATHERED_COPPER_BASIN, "weathered_copper_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(OXIDIZED_COPPER_BASIN, "oxidized_copper_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(WAXED_COPPER_BASIN, "waxed_copper_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(WAXED_EXPOSED_COPPER_BASIN, "waxed_exposed_copper_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(WAXED_WEATHERED_COPPER_BASIN, "waxed_weathered_copper_basin", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(WAXED_OXIDIZED_COPPER_BASIN, "waxed_oxidized_copper_basin", Sandwichable.SANDWICHABLE_ITEMS);

        registerBlock(LETTUCE, "lettuce");
        registerBlock(TOMATOES, "tomatoes");
        registerBlock(ONIONS, "onions");
        registerBlock(CUCUMBERS, "cucumbers");
        registerBlock(ANCIENT_GRAIN, "ancient_grain");

        registerBlock(PICKLE_BRINE, "pickle_brine");

        registerBlock(OAK_CUTTING_BOARD, "oak_cutting_board", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(BIRCH_CUTTING_BOARD, "birch_cutting_board", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(SPRUCE_CUTTING_BOARD, "spruce_cutting_board", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(JUNGLE_CUTTING_BOARD, "jungle_cutting_board", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(ACACIA_CUTTING_BOARD, "acacia_cutting_board", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(DARK_OAK_CUTTING_BOARD, "dark_oak_cutting_board", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(MANGROVE_CUTTING_BOARD, "mangrove_cutting_board", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(CHERRY_CUTTING_BOARD, "cherry_cutting_board", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(BAMBOO_CUTTING_BOARD, "bamboo_cutting_board", Sandwichable.SANDWICHABLE_ITEMS);
        FuelRegistry.INSTANCE.add(OAK_CUTTING_BOARD, 320);
        FuelRegistry.INSTANCE.add(BIRCH_CUTTING_BOARD, 320);
        FuelRegistry.INSTANCE.add(SPRUCE_CUTTING_BOARD, 320);
        FuelRegistry.INSTANCE.add(JUNGLE_CUTTING_BOARD, 320);
        FuelRegistry.INSTANCE.add(ACACIA_CUTTING_BOARD, 320);
        FuelRegistry.INSTANCE.add(DARK_OAK_CUTTING_BOARD, 320);
        FuelRegistry.INSTANCE.add(MANGROVE_CUTTING_BOARD, 320);
        FuelRegistry.INSTANCE.add(CHERRY_CUTTING_BOARD, 320);
        FuelRegistry.INSTANCE.add(BAMBOO_CUTTING_BOARD, 300);
        registerBlock(CRIMSON_CUTTING_BOARD, "crimson_cutting_board", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(WARPED_CUTTING_BOARD, "warped_cutting_board", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(SHRUB, "shrub", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(POTTED_SHRUB, "potted_shrub");
        registerBlock(BOTTLE_CRATE, "bottle_crate", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(PICKLE_JAR, "pickle_jar");
        registerBlock(SALTY_SAND, "salty_sand", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(SALTY_STONE, "salty_stone", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(SALTY_ROCKS, "salty_rocks", Sandwichable.SANDWICHABLE_ITEMS);
        registerBlock(DESALINATOR, "desalinator", Sandwichable.SANDWICHABLE_ITEMS);


        BOTTLECRATE_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Util.id("bottle_crate_ent"), BlockEntityType.Builder.create(BottleCrateBlockEntity::new, BOTTLE_CRATE).build(null));
        DESALINATOR_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Util.id("desalinator_ent"), BlockEntityType.Builder.create(DesalinatorBlockEntity::new, DESALINATOR).build(null));
        CUTTINGBOARD_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Util.id("cutting_board_ent"), BlockEntityType.Builder.create(CuttingBoardBlockEntity::new,
                OAK_CUTTING_BOARD, BIRCH_CUTTING_BOARD, SPRUCE_CUTTING_BOARD, JUNGLE_CUTTING_BOARD, ACACIA_CUTTING_BOARD, DARK_OAK_CUTTING_BOARD, MANGROVE_CUTTING_BOARD, CHERRY_CUTTING_BOARD, BAMBOO_CUTTING_BOARD, CRIMSON_CUTTING_BOARD, WARPED_CUTTING_BOARD
        ).build(null));
        BASIN_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Util.id("basin_ent"), BlockEntityType.Builder.create(BasinBlockEntity::new,
                ANDESITE_BASIN, GRANITE_BASIN, DIORITE_BASIN, BASALT_BASIN, BLACKSTONE_BASIN, DEEPSLATE_BASIN, COPPER_BASIN, EXPOSED_COPPER_BASIN, WEATHERED_COPPER_BASIN, OXIDIZED_COPPER_BASIN, WAXED_COPPER_BASIN, WAXED_EXPOSED_COPPER_BASIN, WAXED_WEATHERED_COPPER_BASIN, WAXED_OXIDIZED_COPPER_BASIN
        ).build(null));
        TOASTER_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Util.id("toaster_ent"), BlockEntityType.Builder.create(ToasterBlockEntity::new, TOASTER).build(null));
        SANDWICHTABLE_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Util.id("sandwich_table_ent"), BlockEntityType.Builder.create(SandwichTableBlockEntity::new, SANDWICH_TABLE).build(null));
        SANDWICH_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Util.id("sandwich_ent"), BlockEntityType.Builder.create(SandwichBlockEntity::new, SANDWICH).build(null));
        PICKLEJAR_BLOCKENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, Util.id("pickle_jar_ent"), BlockEntityType.Builder.create(PickleJarBlockEntity::new, PICKLE_JAR).build(null));
    }

    public static void registerBlock(Block block, String name, ItemGroupQueue queue) {
        registerBlock(block, name);
        var item = Registry.register(Registries.ITEM, Util.id(name), new InfoTooltipBlockItem(block, new Item.Settings()));
        queue.queue(item);
    }

    public static void registerBlock(Block block, String name) {
        Registry.register(Registries.BLOCK, Util.id(name), block);
    }
}
