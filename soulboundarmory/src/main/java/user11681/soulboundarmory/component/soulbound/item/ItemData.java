package user11681.soulboundarmory.component.soulbound.item;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import user11681.soulboundarmory.component.Components;
import user11681.soulboundarmory.registry.Registries;
import user11681.soulboundarmory.util.Util;

public class ItemData implements AutoSyncedComponent<ItemData> {
    protected final ItemStack itemStack;

    public ItemStorage<?> storage;

    public ItemData(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void writeToNbt(final CompoundTag tag) {
        final MinecraftServer server = Util.getServer();

        if (server != null && tag.contains("player") && tag.contains("storage_type")) {
            final Entity entity = server.getPlayerManager().getPlayer(tag.getUuid("player"));
            final StorageType<? extends ItemStorage<?>> type = Registries.STORAGE.get(tag.getString("storage_type"));

            if (type != null && entity != null) {
                this.storage = type.get(entity);
            }
        }
    }

    @Override
    public void readFromNbt(final CompoundTag tag) {
        if (this.storage != null) {
            tag.putUuid("player", this.storage.player.getUuid());
            tag.putString("storage_type", this.storage.getType().asString());
        }

        return tag;
    }

    @Override
    public boolean isComponentEqual(final Component component) {
        return component instanceof ItemData && ItemStack.areItemsEqual(((ItemData) component).itemStack, this.itemStack);
    }
}
