package user11681.phormat.asm.access;

import net.minecraft.util.Formatting;
import user11681.phormat.api.ColorFunction;
import user11681.phormat.api.TextFormatter;

public interface FormattingAccess {
    ColorFunction getColorFunction();

    void setColorFunction(ColorFunction colorFunction);

    TextFormatter getFormatter();

    void setFormatter(TextFormatter formatter);

    boolean isCustom();

    Formatting cast();
}
