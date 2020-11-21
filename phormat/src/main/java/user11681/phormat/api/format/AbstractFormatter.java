package user11681.phormat.api.format;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.text.Style;
import user11681.phormat.asm.access.TextRendererDrawerAccess;

/**
 * A {@linkplain TextFormatter formatter} that stores the arguments from {@linkplain TextFormatter#format TextFormatter#format} in a {@linkplain #context field}.
 */
public abstract class AbstractFormatter implements TextFormatter {
    /**
     * The arguments from {@linkplain TextFormatter#format(TextRendererDrawerAccess, Style, int, int, FontStorage, Glyph, GlyphRenderer, boolean, float, float, float, float, float)}.
     */
    protected final FormattingContext context = new FormattingContext();

    /**
     * Capture the arguments and store them in {@link #context}.
     */
    @Override
    public void format(
        final TextRendererDrawerAccess drawer,
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
        final FormattingContext context = this.context;

        context.drawer = drawer;
        context.style = style;
        context.charIndex = charIndex;
        context.j = j;
        context.storage = storage;
        context.glyph = glyph;
        context.glyphRenderer = glyphRenderer;
        context.isBold = isBold;
        context.red = red;
        context.green = green;
        context.blue = blue;
        context.alpha = alpha;
        context.advance = advance;
    }

    /**
     * Format the current glyph with the information in {@link #context}.
     */
    public abstract void format();
}
