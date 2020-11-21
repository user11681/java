package user11681.commonformatting;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.text.Style;
import user11681.phormat.api.format.TextFormatter;
import user11681.phormat.asm.access.TextRendererDrawerAccess;

public class OverlineFormatter implements TextFormatter {
    public final int yMultiplier;

    public OverlineFormatter(final int level) {
        this.yMultiplier = level;
    }

    @Override
    public void format(final TextRendererDrawerAccess drawer,
                       final Style style,
                       final int charIndex,
                       final int j,
                       final FontStorage storage,
                       final Glyph glyph,
                       final GlyphRenderer glyphRenderer,
                       final boolean isBold,
                       final float red,
                       final float green,
                       final float blue,
                       final float alpha,
                       final float advance) {
        final float shadow = drawer.hasShadow() ? 1 : 0;
        final float x = drawer.x() + shadow;
        final float y = drawer.y() + shadow - 1 - 2 * yMultiplier;

        drawer.invokeAddRectangle(new GlyphRenderer.Rectangle(x - 1, y, x + advance, y - 1, 0.01F, red, green, blue, alpha));
    }
}
