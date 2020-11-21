package user11681.phormat.api.format;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import user11681.phormat.asm.access.TextRendererDrawerAccess;

/**
 * A callback for applying custom formatting on each glyph in a text styled with a custom {@link Formatting}.
 */
@FunctionalInterface
@SuppressWarnings("JavadocReference")
public interface TextFormatter {
    /**
     * This method is called when a glyph is drawn. Apply any custom effects here.
     *
     * @param drawer the {@link TextRenderer.Drawer} instance that draws this glyph.
     * @param style the {@link Style} of the glyph.
     * @param charIndex the index of the current glyph in the text.
     * @param j the height of the character.
     * @param storage the {@link FontStorage} of the current text's font.
     * @param glyph the current glyph.
     * @param glyphRenderer the renderer for the current glyph.
     * @param isBold whether the glyph is bold or not.
     * @param red the red component of this glyph's color.
     * @param green the green component of this glyph's color
     * @param blue the blue component of this glyph's color.
     * @param alpha the alpha component of this glyph's color.
     * @param advance the width of this character.
     */
    void format(
       TextRendererDrawerAccess drawer,
       Style style,
       int charIndex,
       int j,
       FontStorage storage,
       Glyph glyph,
       GlyphRenderer glyphRenderer,
       boolean isBold,
       float red,
       float green,
       float blue,
       float alpha,
       float advance);
}
