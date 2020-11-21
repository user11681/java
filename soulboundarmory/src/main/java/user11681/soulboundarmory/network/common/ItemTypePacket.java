package user11681.soulboundarmory.network.common;

import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import user11681.soulboundarmory.component.soulbound.player.SoulboundItemUtil;

public abstract class ItemTypePacket extends ItemComponentPacket {
    public ItemTypePacket(final Identifier identifier) {
        super(identifier);
    }

    @Override
    protected void accept(final PacketContext context, final ExtendedPacketBuffer buffer) {
        final PlayerEntity player = context.getPlayer();

        player.inventory.removeStack(player.inventory.selectedSlot);
        this.storage.removeOtherItems();
        this.storage.setUnlocked(true);

        SoulboundItemUtil.addItemStack(this.storage.getItemStack(), player);
        this.storage.sync();
        this.storage.refresh();
    }
}
