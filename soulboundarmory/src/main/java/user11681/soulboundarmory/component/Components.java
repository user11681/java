package user11681.soulboundarmory.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import java.util.ArrayList;
import java.util.List;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import nerdhub.cardinal.components.api.event.ItemComponentCallback;
import nerdhub.cardinal.components.api.util.EntityComponents;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import user11681.soulboundarmory.SoulboundArmory;
import user11681.soulboundarmory.component.config.ConfigComponent;
import user11681.soulboundarmory.component.entity.EntityData;
import user11681.soulboundarmory.component.soulbound.item.ItemData;
import user11681.soulboundarmory.component.soulbound.player.SoulboundComponent;
import user11681.soulboundarmory.component.soulbound.player.ToolSoulboundComponent;
import user11681.soulboundarmory.component.soulbound.player.WeaponSoulboundComponent;

public class Components {
    public static final List<ComponentKey<? extends SoulboundComponent>> SOULBOUND_COMPONENTS = new ArrayList<>();

    public static final ComponentKey<ConfigComponent> CONFIG_COMPONENT = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(SoulboundArmory.ID, "config_component"), ConfigComponent.class);
    public static final ComponentKey<EntityData> ENTITY_DATA = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(SoulboundArmory.ID, "entity_data"), EntityData.class);
    public static final ComponentKey<ItemData> ITEM_DATA = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(SoulboundArmory.ID, "item_data"), ItemData.class);
    public static final ComponentKey<SoulboundComponent> TOOL_COMPONENT = registerSoulbound(new Identifier(SoulboundArmory.ID, "tool"), SoulboundComponent.class);
    public static final ComponentKey<SoulboundComponent> WEAPON_COMPONENT = registerSoulbound(new Identifier(SoulboundArmory.ID, "weapon"), SoulboundComponent.class);

    public static <T extends SoulboundComponent> ComponentType<T> registerSoulbound(final Identifier identifier, final Class<T> clazz) {
        final ComponentType<T> type = ComponentRegistry.INSTANCE.registerIfAbsent(identifier, clazz);

        SOULBOUND_COMPONENTS.add(type);

        return type;
    }

    public static List<SoulboundComponent> getComponents(final Entity entity) {
        final List<SoulboundComponent> components = new ArrayList<>();

        for (final ComponentType<? extends SoulboundComponent> type : SOULBOUND_COMPONENTS) {
            components.add(type.get(entity));
        }

        return components;
    }

    public static void setup() {
        EntityComponents.setRespawnCopyStrategy(Components.CONFIG_COMPONENT, RespawnCopyStrategy.ALWAYS_COPY);
        EntityComponents.setRespawnCopyStrategy(Components.TOOL_COMPONENT, RespawnCopyStrategy.ALWAYS_COPY);
        EntityComponents.setRespawnCopyStrategy(Components.WEAPON_COMPONENT, RespawnCopyStrategy.ALWAYS_COPY);

        CONFIG_COMPONENT.attach(EntityComponentCallback.event(PlayerEntity.class), ConfigComponent::new);
        ENTITY_DATA.attach(EntityComponentCallback.event(Entity.class), EntityData::new);
        TOOL_COMPONENT.attach(EntityComponentCallback.event(PlayerEntity.class), ToolSoulboundComponent::new);
        WEAPON_COMPONENT.attach(EntityComponentCallback.event(PlayerEntity.class), WeaponSoulboundComponent::new);

        for (final Item item : Registry.ITEM) {
            ITEM_DATA.attach(ItemComponentCallback.event(item), ItemData::new);
        }
    }
}
