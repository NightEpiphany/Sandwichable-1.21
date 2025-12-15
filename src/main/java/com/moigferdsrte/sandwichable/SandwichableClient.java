package com.moigferdsrte.sandwichable;

import com.moigferdsrte.sandwichable.blocks.ShrubBlock;
import com.moigferdsrte.sandwichable.blocks.entity.renderer.*;
import com.moigferdsrte.sandwichable.blocks.entity.screen.ins.BottleCrateScreen;
import com.moigferdsrte.sandwichable.blocks.entity.screen.ins.DesalinatorScreen;
import com.moigferdsrte.sandwichable.entity.render.SandwichTableMinecartEntityRenderer;
import com.moigferdsrte.sandwichable.fluids.FluidsRegistry;
import com.moigferdsrte.sandwichable.items.BiomeVariantItem;
import com.moigferdsrte.sandwichable.particle.Particles;
import com.moigferdsrte.sandwichable.registry.BlocksRegistry;
import com.moigferdsrte.sandwichable.registry.EntitiesRegistry;
import com.moigferdsrte.sandwichable.registry.ItemsRegistry;
import com.moigferdsrte.sandwichable.registry.NetworkRegistry;
import com.moigferdsrte.sandwichable.util.AncientGrainType;
import com.moigferdsrte.sandwichable.util.RenderFlags;
import com.moigferdsrte.sandwichable.util.SpreadRegistry;
import com.moigferdsrte.sandwichable.util.Util;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;

import java.util.function.Function;

import static net.minecraft.component.DataComponentTypes.CUSTOM_DATA;

public class SandwichableClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        NetworkRegistry.clientInit();
        BlockEntityRendererFactories.register(BlocksRegistry.SANDWICHTABLE_BLOCKENTITY, SandwichTableBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlocksRegistry.SANDWICH_BLOCKENTITY, SandwichBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlocksRegistry.PICKLEJAR_BLOCKENTITY, PickleJarBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlocksRegistry.TOASTER_BLOCKENTITY, ToasterBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlocksRegistry.BASIN_BLOCKENTITY, BasinBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BlocksRegistry.CUTTINGBOARD_BLOCKENTITY, CuttingBoardBlockEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(BasinBlockEntityRenderer.BasinContentModel.MODEL_LAYER, BasinBlockEntityRenderer.BasinContentModel::createModelData);
        EntityModelLayerRegistry.registerModelLayer(PickleJarBlockEntityRenderer.CucumberModel.MODEL_LAYER, PickleJarBlockEntityRenderer.CucumberModel::createModelData);
        EntityModelLayerRegistry.registerModelLayer(PickleJarBlockEntityRenderer.FluidModel.MODEL_LAYER, PickleJarBlockEntityRenderer.FluidModel::createModelData);

        HandledScreens.register(Sandwichable.DESALINATOR_HANDLER, DesalinatorScreen::new);
        HandledScreens.register(Sandwichable.BOTTLE_CRATE_HANDLER, BottleCrateScreen::new);

        ColorProviderRegistry.BLOCK.register(
                (state, view, pos, tintIndex) ->
                {
                    if (!state.get(ShrubBlock.SNIPPED)) {
                        assert view != null;
                        return BiomeColors.getGrassColor(view, pos);
                    } else {
                        return FoliageColors.getDefaultColor();
                    }
                },
                BlocksRegistry.SHRUB, BlocksRegistry.POTTED_SHRUB);

        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
            if (tintIndex != 0) {
                return -1;
            }

            var world = MinecraftClient.getInstance().world;
            if (world != null) {
                return AncientGrainType.get(world.getBiome(pos)).color();
            }
            return FoliageColors.getDefaultColor();
        }, BlocksRegistry.ANCIENT_GRAIN);

        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.SHRUB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.POTTED_SHRUB, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.LETTUCE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.TOMATOES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.CUCUMBERS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.ANCIENT_GRAIN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.ONIONS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksRegistry.PICKLE_JAR, RenderLayer.getCutout());

        EntityRendererRegistry.register(EntitiesRegistry.SANDWICH_TABLE_MINECART, SandwichTableMinecartEntityRenderer::new);

        ColorProviderRegistry.ITEM.register((stack, tintIndex)
                -> tintIndex > 0 ? -1 : GrassColors.getColor(0.5D, 1.0D),
                BlocksRegistry.SHRUB.asItem());

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex <= 0) {
                return -1;
            }
            return ColorHelper.Argb.fullAlpha(AncientGrainType.get(BiomeVariantItem.getBiome(MinecraftClient.getInstance().world, stack)).color());
        }, ItemsRegistry.ANCIENT_GRAIN);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> ColorHelper.Argb.fullAlpha(
                AncientGrainType.get(BiomeVariantItem.getBiome(MinecraftClient.getInstance().world, stack)).color())
                , ItemsRegistry.ANCIENT_GRAIN_BREAD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            var type = AncientGrainType.get(BiomeVariantItem.getBiome(MinecraftClient.getInstance().world, stack));

            return tintIndex > 0 ?  ColorHelper.Argb.fullAlpha(type.color()) :  ColorHelper.Argb.fullAlpha(type.breadColor());
        }, ItemsRegistry.ANCIENT_GRAIN_BREAD_SLICE, ItemsRegistry.TOASTED_ANCIENT_GRAIN_BREAD_SLICE);

        /*ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                    var nbt = stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
                    String type = nbt.getString("spreadType");
                    if(type != null) {
                        if (SpreadRegistry.INSTANCE.fromString(type) != null) {
                            return ColorHelper.Argb.fullAlpha(SpreadRegistry.INSTANCE.fromString(type).getColor(stack));
                        }
                    }
                    return ColorHelper.Argb.fullAlpha(0xFFFFFF);
                },
                ItemsRegistry.SPREAD);*/
        ColorProviderRegistry.ITEM.register((stack, tintIndex)
                        -> ColorHelper.Argb.fullAlpha(SpreadRegistry.MUSHROOM_STEW.getColor()),
                ItemsRegistry.MUSHROOM_STEW_SPREAD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex)
                        -> ColorHelper.Argb.fullAlpha(SpreadRegistry.MAYONNAISE.getColor()),
                ItemsRegistry.MAYONNAISE_SPREAD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex)
                        -> ColorHelper.Argb.fullAlpha(SpreadRegistry.FERMENTING_MILK.getColor()),
                ItemsRegistry.FERMENTING_MILK_SPREAD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex)
                        -> ColorHelper.Argb.fullAlpha(SpreadRegistry.GLOW_BERRY_JAM.getColor()),
                ItemsRegistry.GLOW_BERRY_JAM_SPREAD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex)
                        -> ColorHelper.Argb.fullAlpha(SpreadRegistry.SWEET_BERRY_JAM.getColor()),
                ItemsRegistry.SWEET_BERRY_JAM_SPREAD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex)
                        -> ColorHelper.Argb.fullAlpha(SpreadRegistry.HONEY.getColor()),
                ItemsRegistry.HONEY_SPREAD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex)
                        -> ColorHelper.Argb.fullAlpha(SpreadRegistry.POTION.getColor()),
                ItemsRegistry.POTION_SPREAD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex)
                        -> ColorHelper.Argb.fullAlpha(SpreadRegistry.SUSPICIOUS_STEW.getColor()),
                ItemsRegistry.SUSPICIOUS_STEW_SPREAD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex)
                        -> ColorHelper.Argb.fullAlpha(SpreadRegistry.RABBIT_STEW.getColor()),
                ItemsRegistry.RABBIT_STEW_SPREAD);

        ColorProviderRegistry.ITEM.register((stack, tintIndex)
                        -> ColorHelper.Argb.fullAlpha(SpreadRegistry.BEETROOT_SOUP.getColor()),
                ItemsRegistry.BEETROOT_SOUP_SPREAD);

        ModelPredicateProviderRegistry.register(ItemsRegistry.MUSHROOM_STEW_SPREAD, Util.id("loaf_shape"), (stack, world, entity, seed) -> {
            if(stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).contains("onLoaf")) return stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("onLoaf") ? 1 : 0;
            return 0;
        });

        ModelPredicateProviderRegistry.register(ItemsRegistry.MAYONNAISE_SPREAD, Util.id("loaf_shape"), (stack, world, entity, seed) -> {
            if(stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).contains("onLoaf")) return stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("onLoaf") ? 1 : 0;
            return 0;
        });

        ModelPredicateProviderRegistry.register(ItemsRegistry.BEETROOT_SOUP_SPREAD, Util.id("loaf_shape"), (stack, world, entity, seed) -> {
            if(stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).contains("onLoaf")) return stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("onLoaf") ? 1 : 0;
            return 0;
        });

        ModelPredicateProviderRegistry.register(ItemsRegistry.HONEY_SPREAD, Util.id("loaf_shape"), (stack, world, entity, seed) -> {
            if(stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).contains("onLoaf")) return stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("onLoaf") ? 1 : 0;
            return 0;
        });

        ModelPredicateProviderRegistry.register(ItemsRegistry.SUSPICIOUS_STEW_SPREAD, Util.id("loaf_shape"), (stack, world, entity, seed) -> {
            if(stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).contains("onLoaf")) return stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("onLoaf") ? 1 : 0;
            return 0;
        });

        ModelPredicateProviderRegistry.register(ItemsRegistry.SWEET_BERRY_JAM_SPREAD, Util.id("loaf_shape"), (stack, world, entity, seed) -> {
            if(stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).contains("onLoaf")) return stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("onLoaf") ? 1 : 0;
            return 0;
        });

        ModelPredicateProviderRegistry.register(ItemsRegistry.GLOW_BERRY_JAM_SPREAD, Util.id("loaf_shape"), (stack, world, entity, seed) -> {
            if(stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).contains("onLoaf")) return stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("onLoaf") ? 1 : 0;
            return 0;
        });

        ModelPredicateProviderRegistry.register(ItemsRegistry.POTION_SPREAD, Util.id("loaf_shape"), (stack, world, entity, seed) -> {
            if(stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).contains("onLoaf")) return stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("onLoaf") ? 1 : 0;
            return 0;
        });

        ModelPredicateProviderRegistry.register(ItemsRegistry.FERMENTING_MILK_SPREAD, Util.id("loaf_shape"), (stack, world, entity, seed) -> {
            if(stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).contains("onLoaf")) return stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("onLoaf") ? 1 : 0;
            return 0;
        });

        ModelPredicateProviderRegistry.register(ItemsRegistry.RABBIT_STEW_SPREAD, Util.id("loaf_shape"), (stack, world, entity, seed) -> {
            if(stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).contains("onLoaf")) return stack.getOrDefault(CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean("onLoaf") ? 1 : 0;
            return 0;
        });

        ModelPredicateProviderRegistry.register(Util.id("sandwich_state"), (stack, world, entity, seed) -> {
            if(entity == null) return RenderFlags.RENDERING_SANDWICH_ITEM * 0.25f;
            return 0;
        });

        setupPickleBrine();

        Particles.init();
    }

    private static void setupPickleBrine() {
        Fluid still = FluidsRegistry.PICKLE_BRINE;
        Fluid flowing = FluidsRegistry.PICKLE_BRINE_FLOWING;
        Identifier fluidTexture = Util.id("pickle_brine");
        Identifier stillId = Identifier.of(fluidTexture.getNamespace(), "block/" + fluidTexture.getPath());
        Identifier flowingId = Identifier.of(fluidTexture.getNamespace(), "block/" + fluidTexture.getPath() + "_flow");

        /*
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
            registry.register(stillId);
            registry.register(flowingId);
        });

         */

        Identifier fluidId = Registries.FLUID.getId(still);
        Identifier listenerId = Identifier.of(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");

        Sprite[] sprites = { null, null };

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public void reload(ResourceManager manager) {
                final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
                sprites[0] = atlas.apply(stillId);
                sprites[1] = atlas.apply(flowingId);
            }

            @Override
            public Identifier getFabricId() {
                return listenerId;
            }
        });

        FluidRenderHandler renderHandler = new FluidRenderHandler() {
            @Override
            public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
                return sprites;
            }

            @Override
            public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
                return 0x65ff6e;
            }
        };

        FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
        FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), FluidsRegistry.PICKLE_BRINE, FluidsRegistry.PICKLE_BRINE_FLOWING);
    }
}
