package user11681.soulboundarmory.network.C2S;

import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import user11681.soulboundarmory.SoulboundArmory;
import user11681.soulboundarmory.network.common.ExtendedPacketBuffer;
import user11681.soulboundarmory.network.common.ItemTypePacket;

public class C2SItemType extends ItemTypePacket {
    public C2SItemType() {
        super(new Identifier(SoulboundArmory.ID, "server_item_type"));
    }

    @Override
    protected void accept(final PacketContext context, final ExtendedPacketBuffer buffer) {
        super.accept(context, buffer);

        final PlayerEntity player = context.getPlayer();

        player.inventory.setStack(buffer.readInt(), this.storage.getItemStack());
        this.storage.getComponent().setCurrentItem(this.storage);
        this.storage.removeOtherItems();
        this.storage.sync();
    }
}
