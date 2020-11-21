package user11681.soulboundarmory.registry;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import user11681.soulboundarmory.SoulboundArmory;
import user11681.soulboundarmory.item.SoulboundDaggerItem;
import user11681.soulboundarmory.item.SoulboundGreatswordItem;
import user11681.soulboundarmory.item.SoulboundPickItem;
import user11681.soulboundarmory.item.SoulboundStaffItem;
import user11681.soulboundarmory.item.SoulboundSwordItem;

public final class SoulboundItems {
    public static final SoulboundDaggerItem SOULBOUND_DAGGER = new SoulboundDaggerItem();
    public static final SoulboundSwordItem SOULBOUND_SWORD = new SoulboundSwordItem();
    public static final SoulboundGreatswordItem SOULBOUND_GREATSWORD = new SoulboundGreatswordItem();
    public static final SoulboundStaffItem SOULBOUND_STAFF = new SoulboundStaffItem();
    public static final SoulboundPickItem SOULBOUND_PICK = new SoulboundPickItem();

    private static void register(final Identifier identifier, final Item item) {
        Registry.register(Registry.ITEM, identifier, item);
    }

    public static void register() {
        register(new Identifier(SoulboundArmory.ID, "soulbound_dagger"), SOULBOUND_DAGGER);
        register(new Identifier(SoulboundArmory.ID, "soulbound_sword"), SOULBOUND_SWORD);
        register(new Identifier(SoulboundArmory.ID, "soulbound_greatsword"), SOULBOUND_GREATSWORD);
        register(new Identifier(SoulboundArmory.ID, "soulbound_staff"), SOULBOUND_STAFF);
        register(new Identifier(SoulboundArmory.ID, "soulbound_pick"), SOULBOUND_PICK);
    }
}
