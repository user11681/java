package user11681.phormat.asm.access;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public interface TextRendererDrawerAccess {
    int light();

    float brightnessMultiplier();

    float red();

    float green();

    float blue();

    float alpha();

    float x();

    float y();

    boolean hasShadow();

    boolean isTranslucent();

    Matrix4f matrix();

    List<GlyphRenderer.Rectangle> rectangles();

    VertexConsumerProvider vertexConsumers();

    float invokeDrawLayer(int underlineColor, float x);

    void invokeAddRectangle(GlyphRenderer.Rectangle rectangle);
}
