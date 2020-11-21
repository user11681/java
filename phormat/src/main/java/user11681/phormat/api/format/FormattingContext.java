package user11681.phormat.api.format;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.text.Style;
import user11681.phormat.asm.access.TextRendererDrawerAccess;

/**
 * A container for holding text formatting from {@link AbstractFormatter#format} for use in {@link AbstractFormatter#format()}.
 */
public class FormattingContext {
    public TextRendererDrawerAccess drawer;
    public Style style;
    public int charIndex;
    public int j;
    public FontStorage storage;
    public Glyph glyph;
    public GlyphRenderer glyphRenderer;
    public boolean isBold;
    public float red;
    public float green;
    public float blue;
    public float alpha;
    public float advance;
}
