package user11681.soulboundarmory.component.soulbound.player;

import javax.annotation.Nonnull;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import user11681.soulboundarmory.component.Components;
import user11681.soulboundarmory.component.soulbound.item.weapon.DaggerStorage;
import user11681.soulboundarmory.component.soulbound.item.weapon.GreatswordStorage;
import user11681.soulboundarmory.component.soulbound.item.weapon.StaffStorage;
import user11681.soulboundarmory.component.soulbound.item.weapon.SwordStorage;
import user11681.soulboundarmory.registry.SoulboundItems;

public class WeaponSoulboundComponent extends SoulboundComponent {
    public WeaponSoulboundComponent(final PlayerEntity player) {
        super(player);

        this.store(new DaggerStorage(this, SoulboundItems.SOULBOUND_DAGGER));
        this.store(new SwordStorage(this, SoulboundItems.SOULBOUND_SWORD));
        this.store(new GreatswordStorage(this, SoulboundItems.SOULBOUND_GREATSWORD));
        this.store(new StaffStorage(this, SoulboundItems.SOULBOUND_STAFF));
    }

    @Nonnull
    @Override
    public ComponentType<SoulboundComponent> getComponentType() {
        return Components.WEAPON_COMPONENT;
    }
}
