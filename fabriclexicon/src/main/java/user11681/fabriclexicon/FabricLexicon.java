package user11681.fabriclexicon;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import user11681.fabriclexicon.client.FabricLexiconScreen;
import user11681.fabriclexicon.component.FabricLexiconComponent;
import user11681.fabriclexicon.item.FabricLexiconItem;

public class FabricLexicon implements ModInitializer, ClientModInitializer, EntityComponentInitializer {
    public static final ComponentKey<FabricLexiconComponent> componentKey = ComponentRegistryV3.INSTANCE.getOrCreate(id("fabric_lexicon"), FabricLexiconComponent.class);

    public static final String ID = "fabriclexicon";

    public static Identifier id(final String path) {
        return new Identifier(ID, path);
    }

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, id("fabric_lexicon"), FabricLexiconItem.instance);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(FabricLexiconScreenHandler.type, FabricLexiconScreen::new);
    }

    @Override
    public void registerEntityComponentFactories(final EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(componentKey, FabricLexiconComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
