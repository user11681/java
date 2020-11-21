package user11681.phormat.impl;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import user11681.phormat.api.FormattingRegistry;

public final class FormattingRegistryImpl implements FormattingRegistry {
    public final List<FormattingInfo> entries = new ReferenceArrayList<>();

    @Override
    public void register(final String name, final char code, final int colorIndex, @Nullable final Integer color) {
        this.register(name, code);

        PhormatInitializer.colorAdder.addEnum(name, name, code, colorIndex, color);
    }

    @Override
    public void register(final String name, final char code, final boolean modifier) {
        this.register(name, code);

        PhormatInitializer.modifierAdder.addEnum(name, name, code, modifier);
    }

    @Override
    public void register(final String name, final char code, final boolean modifier, final int colorIndex, @Nullable final Integer color) {
        this.register(name, code);

        PhormatInitializer.primaryAdder.addEnum(name, name, code, modifier, colorIndex, color);
    }

    private void register(final String name, final char code) {
        if (!PhormatInitializer.initializing) {
            throw new IllegalStateException("all custom Formatting entries must be registered during the \"phormat:init\" entrypoint.");
        }

        if (PhormatInitializer.names.contains(name)) {
            throw new IllegalArgumentException(String.format("a Formatting with name %s already exists.", name));
        }

        this.entries.add(new FormattingInfo(name, code));
    }
}
