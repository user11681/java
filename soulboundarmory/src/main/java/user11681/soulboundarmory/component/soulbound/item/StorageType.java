package user11681.soulboundarmory.component.soulbound.item;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import user11681.soulboundarmory.SoulboundArmory;
import user11681.soulboundarmory.component.Components;
import user11681.soulboundarmory.component.soulbound.item.tool.PickStorage;
import user11681.soulboundarmory.component.soulbound.item.weapon.DaggerStorage;
import user11681.soulboundarmory.component.soulbound.item.weapon.GreatswordStorage;
import user11681.soulboundarmory.component.soulbound.item.weapon.StaffStorage;
import user11681.soulboundarmory.component.soulbound.item.weapon.SwordStorage;
import user11681.soulboundarmory.component.soulbound.player.SoulboundComponent;
import user11681.soulboundarmory.registry.Registries;
import user11681.usersmanual.registry.AbstractRegistryEntry;

public class StorageType<T extends ItemStorage<T>> extends AbstractRegistryEntry {
    public static final StorageType<DaggerStorage> DAGGER = Registries.STORAGE.register(new StorageType<>(new Identifier(SoulboundArmory.ID, "dagger")));
    public static final StorageType<SwordStorage> SWORD = Registries.STORAGE.register(new StorageType<>(new Identifier(SoulboundArmory.ID, "sword")));
    public static final StorageType<GreatswordStorage> GREATSWORD = Registries.STORAGE.register(new StorageType<>(new Identifier(SoulboundArmory.ID, "greatsword")));
    public static final StorageType<StaffStorage> STAFF = Registries.STORAGE.register(new StorageType<>(new Identifier(SoulboundArmory.ID, "staff")));
    public static final StorageType<PickStorage> PICK = Registries.STORAGE.register(new StorageType<>(new Identifier(SoulboundArmory.ID, "pick")));

    public StorageType(final Identifier identifier) {
        super(identifier);
    }

    public static List<ItemStorage<?>> getStorages(final Entity entity) {
        final List<ItemStorage<?>> storages = new ArrayList<>();

        for (final SoulboundComponent component : Components.getComponents(entity)) {
            storages.addAll(component.getStorages().values());
        }

        return storages;
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    public static <T extends ItemStorage<T>> ItemStorage<T> get(final Entity entity, final Item item) {
        for (final ItemStorage<?> storage : getStorages(entity)) {
            if (storage.getItem() == item) {
                //noinspection unchecked
                return (ItemStorage<T>) storage;
            }
        }

        return null;
    }

    public static ItemStorage<?> getFirstMenuStorage(final Entity entity) {
        if (entity == null) {
            return null;
        }

        for (final ItemStack itemStack : entity.getItemsHand()) {
            final Item item = itemStack.getItem();

            for (final SoulboundComponent component : Components.getComponents(entity)) {
                for (final ItemStorage<?> storage : component.getStorages().values()) {
                    if (storage.getItem() == item || storage.canConsume(item)) {
                        return storage;
                    }
                }
            }
        }

        return null;
    }

    public T get(final Entity entity) {
        for (final SoulboundComponent component : Components.getComponents(entity)) {
            final T storage = this.get(component);

            if (storage != null) {
                return storage;
            }
        }

        return null;
    }

    public T get(final SoulboundComponent component) {
        return component.getStorage(this);
    }
}
