package user11681.phormat.impl;

import com.chocohead.mm.api.ClassTinkerers;
import com.chocohead.mm.api.EnumAdder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus.Internal;
import user11681.dynamicentry.DynamicEntry;

@Internal
public class PhormatInitializer implements Runnable {
    public static ReferenceOpenHashSet<FormattingInfo> formattingInfo = new ReferenceOpenHashSet<>();
    public static ObjectOpenHashSet<String> names = new ObjectOpenHashSet<>();
    public static EnumAdder colorAdder;
    public static EnumAdder modifierAdder;
    public static EnumAdder primaryAdder;
    public static boolean initializing;

    @Override
    public void run() {
        initializing = true;

        DynamicEntry.tryExecute("phormat:init", Runnable.class, Runnable::run);

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
