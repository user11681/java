package user11681.thorium.client.render.block.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import user11681.thorium.block.entity.SoulSandBlockEntity;

public class SoulSandBlockEntityRenderer extends BlockEntityRenderer<SoulSandBlockEntity> {
    public SoulSandBlockEntityRenderer(final BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(final SoulSandBlockEntity blockEntity, final float tickDelta, final MatrixStack matrices,
                       final VertexConsumerProvider vertexConsumers, final int light, final int overlay) {
        matrices.push();
        matrices.pop();
    }
}
