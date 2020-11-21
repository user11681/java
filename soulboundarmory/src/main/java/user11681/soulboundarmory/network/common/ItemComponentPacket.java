package user11681.soulboundarmory.network.common;

import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import user11681.soulboundarmory.component.soulbound.item.ItemStorage;
import user11681.soulboundarmory.registry.Registries;

public abstract class ItemComponentPacket extends Packet {
    protected ItemStorage<?> storage;

    public ItemComponentPacket(final Identifier identifier) {
        super(identifier);
    }

    @Override
    public void accept(final PacketContext context, final PacketByteBuf buffer) {
        this.storage = Registries.STORAGE.get(buffer.readIdentifier()).get(context.getPlayer());

        super.accept(context, buffer);
    }
}
