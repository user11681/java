package user11681.persistententities.config;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config.Gui.Background;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Tooltip;
import net.fabricmc.api.ModInitializer;

@Config(name = "persistententities")
@Background("textures/block/andesite.png")
public class PersistentEntitiesConfiguration implements ConfigData, ModInitializer {
    @Tooltip
    int itemTime = 6000;

    @Tooltip
    int mobTime = 600;

    boolean disableItems = true;
    boolean disableMobs = true;

    public static class Data {
        public static int itemTime;
        public static int mobTime;
        public static boolean disableItems;
        public static boolean disableMobs;

        public static void set(final PersistentEntitiesConfiguration configuration) {
            itemTime = configuration.itemTime;
            mobTime = configuration.mobTime;
            disableItems = configuration.disableItems;
            disableMobs = configuration.disableMobs;
        }
    }

    @Override
    public void validatePostLoad() {
        Data.set(this);
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(PersistentEntitiesConfiguration.class, PersistentEntitiesSerializer::new);
    }
}
