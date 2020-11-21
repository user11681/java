package user11681.phormat.api;

import net.minecraft.text.Style;

/**
 * A callback that produces a new color based on
 */
@FunctionalInterface
public interface ColorFunction {
    /**
     * @param previous the previous color of the {@link Style}.
     * @return the new color of the {@link Style}.
     */
    int apply(int previous);
}
