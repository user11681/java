package user11681.soulboundarmory.network.C2S;

import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import user11681.soulboundarmory.SoulboundArmory;
import user11681.soulboundarmory.component.statistics.StatisticType;
import user11681.soulboundarmory.network.common.ExtendedPacketBuffer;
import user11681.soulboundarmory.network.common.ItemComponentPacket;

public class C2SEnchant extends ItemComponentPacket {
    public C2SEnchant() {
        super(new Identifier(SoulboundArmory.ID, "server_enchant"));
    }

    @Override
    protected void accept(final PacketContext context, final ExtendedPacketBuffer buffer) {
        final Enchantment enchantment = Registry.ENCHANTMENT.get(buffer.readIdentifier());
        final boolean add = buffer.readBoolean();
        int change = add ? 1 : -1;

        if (buffer.readBoolean()) {
            if (add) {
                change *= this.storage.getDatum(StatisticType.ENCHANTMENT_POINTS);
            } else {
                change *= this.storage.getEnchantment(enchantment);
            }
        }

        this.storage.addEnchantment(enchantment, change);
        this.storage.refresh();
    }
}
