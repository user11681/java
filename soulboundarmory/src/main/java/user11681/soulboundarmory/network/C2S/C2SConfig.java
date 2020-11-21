package user11681.soulboundarmory.network.C2S;

import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.util.Identifier;
import user11681.soulboundarmory.SoulboundArmory;
import user11681.soulboundarmory.component.Components;
import user11681.soulboundarmory.component.config.ConfigComponent;
import user11681.soulboundarmory.network.common.ExtendedPacketBuffer;
import user11681.soulboundarmory.network.common.Packet;

public class C2SConfig extends Packet {
    public C2SConfig() {
        super(new Identifier(SoulboundArmory.ID, "server_config"));
    }

    @Override
    protected void accept(final PacketContext context, final ExtendedPacketBuffer buffer) {
        final ConfigComponent component = Components.CONFIG_COMPONENT.get(context.getPlayer());

        component.setAddToOffhand(buffer.readBoolean());
        component.setLevelupNotifications(buffer.readBoolean());
    }
}
