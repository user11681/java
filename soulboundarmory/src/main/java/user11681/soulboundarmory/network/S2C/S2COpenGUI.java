package user11681.soulboundarmory.network.S2C;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.util.Identifier;
import user11681.soulboundarmory.SoulboundArmory;
import user11681.soulboundarmory.client.gui.screen.tab.SoulboundTab;
import user11681.soulboundarmory.network.common.ExtendedPacketBuffer;
import user11681.soulboundarmory.network.common.ItemComponentPacket;

import static user11681.soulboundarmory.SoulboundArmoryClient.CLIENT;

public class S2COpenGUI extends ItemComponentPacket {
    public S2COpenGUI() {
        super(new Identifier(SoulboundArmory.ID, "client_open_gui"));
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected void accept(final PacketContext context, final ExtendedPacketBuffer buffer) {
        if (CLIENT.currentScreen instanceof SoulboundTab) {
            this.storage.openGUI(buffer.readInt());
        }
    }
}
