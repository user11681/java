package user11681.phormat.impl;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class FormattingInfo {
    public final String name;
    public final char code;

    public FormattingInfo(final String name, final char code) {
        this.name = name;
        this.code = code;
    }
}
