package user11681.phormat.api;

import org.jetbrains.annotations.Nullable;
import user11681.phormat.impl.FormattingInfo;
import user11681.phormat.impl.PhormatInitializer;

public class FormattingBuilder {
    private final String name;
    private final char code;

    private TextFormatter formatter;
    private ColorFunction colorFunction;

    public static FormattingBuilder create(final String name, final char code) {
        if (!PhormatInitializer.initializing) {
            throw new IllegalStateException("all instances of FormattingBuilder must be constructed during the \"phormat:init\" entrypoint.");
        }

        if (PhormatInitializer.names.contains(name)) {
            throw new IllegalArgumentException(String.format("a formatting with name %s already exists.", name));
        }

        return new FormattingBuilder(name, code);
    }

    private FormattingBuilder(final String name, final char code) {
        this.name = name;
        this.code = code;
    }

    public FormattingBuilder formatter(final TextFormatter formatter) {
        this.formatter = formatter;

        return this;
    }

    public FormattingBuilder color(final ColorFunction colorFunction) {
        this.colorFunction = colorFunction;

        return this;
    }

    public void build(final int colorIndex, @Nullable final Integer color) {
        PhormatInitializer.colorAdder.addEnum(this.name, this.name, this.code, colorIndex, color);

        this.build();
    }

    public void build(final boolean modifier) {
        PhormatInitializer.modifierAdder.addEnum(this.name, this.name, this.code, modifier);

        this.build();
    }

    public void build(final boolean modifier, final int colorIndex, @Nullable final Integer color) {
        PhormatInitializer.primaryAdder.addEnum(this.name, this.name, this.code, modifier, colorIndex, color);

        this.build();
    }

    private void build() {
        PhormatInitializer.formattingInfo.add(new FormattingInfo(this.name, this.code, this.colorFunction, this.formatter));
    }
}
