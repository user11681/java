package user11681.phormat.api;

import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

/**
 * A registry for custom {@link Formatting} entries. Each method is mapped to each constructor of {@link Formatting}.
 */
public interface FormattingRegistry {
    /**
     * Construct a {@link Formatting}.
     *
     * @param name the {@linkplain Enum#name() name} of the {@link Formatting}.
     * @param code the character code of the {@link Formatting}.
     * @param colorIndex the index of the {@link Formatting}'s color.
     * @param color the color applied by the {@link Formatting}.
     */
    void register(String name, char code, int colorIndex, @Nullable Integer color);

    /**
     * Construct a {@link Formatting}.
     *
     * @param name the {@linkplain Enum#name() name} of the {@link Formatting}.
     * @param code the character code of the {@link Formatting}.
     * @param modifier whether the {@link Formatting} is a modifier like underline and strikethrough or not.
     */
    void register(String name, char code, boolean modifier);

    /**
     * Construct a {@link Formatting}.
     *
     * @param name the {@linkplain Enum#name() name} of the {@link Formatting}.
     * @param code the character code of the {@link Formatting}.
     * @param modifier whether the {@link Formatting} is a modifier like underline and strikethrough or not.
     * @param colorIndex the index of the {@link Formatting}'s color.
     * @param color the color applied by the {@link Formatting}.
     */
    void register(String name, char code, boolean modifier, int colorIndex, @Nullable Integer color);
}
