package user11681.thorium;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user11681.thorium.config.ThoriumConfiguration;

public class Thorium implements ModInitializer {
    public static final String MOD_ID = "arbitrarypatches";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static Identifier id(final String id) {
        return new Identifier(MOD_ID, id);
    }

    @Override
    public void onInitialize() {
        AutoConfig.register(ThoriumConfiguration.class, Toml4jConfigSerializer::new);
    }
}
