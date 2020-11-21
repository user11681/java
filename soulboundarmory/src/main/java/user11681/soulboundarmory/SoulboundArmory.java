package user11681.soulboundarmory;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user11681.soulboundarmory.command.SoulboundArmoryCommand;
import user11681.soulboundarmory.component.Components;
import user11681.soulboundarmory.config.Configuration;
import user11681.soulboundarmory.enchantment.ImpactEnchantment;
import user11681.soulboundarmory.registry.SoulboundItems;
import user11681.soulboundarmory.registry.Packets;

public class SoulboundArmory implements ModInitializer {
    public static final EnvType ENVIRONMENT = FabricLoader.getInstance().getEnvironmentType();

    public static final String ID = "soulboundarmory";
    public static final String NAME = "soulbound armory";

    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static final Enchantment IMPACT = Registry.register(Registry.ENCHANTMENT, new Identifier(ID, "impact"), new ImpactEnchantment());

    public static final ServerSidePacketRegistry PACKET_REGISTRY = ServerSidePacketRegistry.INSTANCE;

    public static String key(final String name) {
        return String.format("key.%s.%s", ID, name);
    }

    public static String category(final String name) {
        return String.format("key.categories.%s", name);
    }

    public void onInitialize() {
        SoulboundItems.register();
        Components.setup();
        Packets.register();

        CommandRegistrationCallback.EVENT.register(SoulboundArmoryCommand::register);

        AutoConfig.register(Configuration.class, Toml4jConfigSerializer::new);
    }
}
