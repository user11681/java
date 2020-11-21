package user11681.phormat.impl;

import org.jetbrains.annotations.ApiStatus.Internal;
import user11681.phormat.api.ColorFunction;
import user11681.phormat.api.TextFormatter;

@Internal
public class FormattingInfo {
    public final String name;
    public final char code;
    public final ColorFunction colorFunction;
    public final TextFormatter formatter;

    public FormattingInfo(final String name, final char code, final ColorFunction colorFunction, final TextFormatter formatter) {
        this.name = name;
        this.code = code;
        this.colorFunction = colorFunction;
        this.formatter = formatter;
    }
}
