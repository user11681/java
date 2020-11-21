package user11681.phormat.impl;

import com.chocohead.mm.api.ClassTinkerers;
import com.chocohead.mm.api.EnumAdder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.Map;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import org.jetbrains.annotations.ApiStatus.Internal;
import user11681.phormat.api.FormattingInitializer;

@Internal
public class PhormatInitializer implements Runnable {
    public static Map<FormattingInitializer, FormattingRegistryImpl> registries = new Reference2ReferenceOpenHashMap<>();
    public static Set<String> names = new ObjectOpenHashSet<>();
    public static EnumAdder colorAdder;
    public static EnumAdder modifierAdder;
    public static EnumAdder primaryAdder;
    public static boolean initializing;

    @Override
    public void run() {
        initializing = true;

        for (final EntrypointContainer<FormattingInitializer> container : FabricLoader.getInstance().getEntrypointContainers("phormat:init", FormattingInitializer.class)) {
            final ModContainer mod = container.getProvider();
            final FormattingInitializer entrypoint = container.getEntrypoint();
            FormattingRegistryImpl registry = registries.get(entrypoint);

            if (registry == null) {
                registry = new FormattingRegistryImpl();

                registries.put(entrypoint, registry);
            }

            entrypoint.register(registry);
        }

        initializing = false;

        colorAdder.build();
        modifierAdder.build();
        primaryAdder.build();

        names = null;
        colorAdder = null;
        modifierAdder = null;
        primaryAdder = null;
    }

    static {
        final String Formatting = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_124");

        PhormatInitializer.colorAdder = ClassTinkerers.enumBuilder(Formatting, String.class, char.class, int.class, Integer.class);
        PhormatInitializer.modifierAdder = ClassTinkerers.enumBuilder(Formatting, String.class, char.class, boolean.class);
        PhormatInitializer.primaryAdder = ClassTinkerers.enumBuilder(Formatting, String.class, char.class, boolean.class, int.class, Integer.class);
    }
}
