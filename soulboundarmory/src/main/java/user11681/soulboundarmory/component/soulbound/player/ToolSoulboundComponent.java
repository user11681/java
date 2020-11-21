package user11681.soulboundarmory.component.soulbound.player;

import javax.annotation.Nonnull;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import user11681.soulboundarmory.component.Components;
import user11681.soulboundarmory.component.soulbound.item.tool.PickStorage;
import user11681.soulboundarmory.registry.SoulboundItems;

public class ToolSoulboundComponent extends SoulboundComponent {
    public ToolSoulboundComponent(final PlayerEntity player) {
        super(player);

        this.store(new PickStorage(this, SoulboundItems.SOULBOUND_PICK));
    }

    @Nonnull
    @Override
    public ComponentType<SoulboundComponent> getComponentType() {
        return Components.WEAPON_COMPONENT;
    }
}
