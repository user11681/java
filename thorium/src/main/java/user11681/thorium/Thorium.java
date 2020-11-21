package user11681.thorium;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user11681.thorium.block.entity.SoulSandBlockEntity;
import user11681.thorium.config.ThoriumConfiguration;

public class Thorium implements ModInitializer {
    public static final String MOD_ID = "arbitrarypatches";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final BlockEntityType<SoulSandBlockEntity> SOUL_SAND = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(Thorium.MOD_ID, "soul_sand"),
            BlockEntityType.Builder.create(SoulSandBlockEntity::new, Blocks.SOUL_SAND).build(null)
    );

    @Override
    public void onInitialize() {
        AutoConfig.register(ThoriumConfiguration.class, Toml4jConfigSerializer::new);
    }
}
