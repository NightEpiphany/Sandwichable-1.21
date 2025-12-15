package com.moigferdsrte.sandwichable.blocks.entity.renderer;

import com.moigferdsrte.sandwichable.blocks.entity.BasinBlockEntity;
import com.moigferdsrte.sandwichable.blocks.extra.BasinContent;
import com.moigferdsrte.sandwichable.blocks.extra.BasinContentType;
import com.moigferdsrte.sandwichable.util.Util;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class BasinBlockEntityRenderer implements BlockEntityRenderer<BasinBlockEntity> {
    private static final Identifier TEX_MILK = Util.id("textures/entity/basin/milk.png");

    private final BasinContentModel model;

    public BasinBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        model = new BasinContentModel(context);
    }

    @Override
    public void render(BasinBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        Identifier cheeseTex = blockEntity.getContent().getCheeseType().getTexture();
        if(blockEntity.getContent() == BasinContent.MILK) {
            this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEX_MILK)), light, overlay, ColorHelper.Argb.fromFloats(1.0F, 1.0F, 1.0F, 1.0F));
        }
        else if(blockEntity.getContent().getContentType() == BasinContentType.FERMENTING_MILK) {
            float progressToFloatInv = 1.0F - (float)blockEntity.getFermentProgress() / BasinBlockEntity.fermentTime;
            this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(cheeseTex)), light, overlay, ColorHelper.Argb.fromFloats(1.0F, 1.0F, 1.0F, 1.0F));
            this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEX_MILK)), light, overlay, ColorHelper.Argb.fromFloats(progressToFloatInv, 1.0F, 1.0F, 1.0F));
        }
        else if(blockEntity.getContent().getContentType() == BasinContentType.CHEESE) {
            this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(cheeseTex)), light, overlay, ColorHelper.Argb.fromFloats(1.0F, 1.0F, 1.0F, 1.0F));
        }
        matrices.pop();
    }

    public static class BasinContentModel extends Model {
        public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Util.id("basin/content"), "main");

        private final ModelPart part;

        public BasinContentModel(BlockEntityRendererFactory.Context ctx) {
            super(RenderLayer::getEntityTranslucent);
            this.part = ctx.getLayerModelPart(MODEL_LAYER).getChild("main");
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
            this.part.render(matrices, vertices, light, overlay, color);
        }

        public static TexturedModelData createModelData() {
            var data = new ModelData();
            data.getRoot().addChild("main",
                    ModelPartBuilder.create().cuboid(3, 2, 3, 10, 4, 10),
                    ModelTransform.NONE
            );
            return TexturedModelData.of(data, 64, 32);
        }
    }
}
