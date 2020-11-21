package user11681.soulboundarmory.component.statistics;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import nerdhub.cardinal.components.api.util.NbtSerializable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import user11681.usersmanual.collections.OrderedArrayMap;

public class EnchantmentStorage extends OrderedArrayMap<Enchantment, Integer> implements NbtSerializable {
    public EnchantmentStorage(final Predicate<Enchantment> predicate) {
        super(() -> 0, Registry.ENCHANTMENT);

        this.removeIf(predicate.negate());
    }

    @Override
    public Integer get(final Object enchantment) {
        final Integer result = super.get(enchantment);

        return result == null ? 0 : result;
    }

    public void add(final Enchantment enchantment, final int value) {
        this.put(enchantment, this.get(enchantment) + value);
    }

    public void reset() {
        for (final Enchantment enchantment : this) {
            this.put(enchantment, 0);
        }
    }

    @Override
    public void fromTag(final CompoundTag tag) {
        final Registry<Enchantment> registry = Registry.ENCHANTMENT;

        for (final String key : tag.getKeys()) {
            final Enchantment enchantment = registry.get(new Identifier(key));

            if (this.containsKey(enchantment)) {
                this.put(enchantment, tag.getInt(key));
            }
        }
    }

    @Nonnull
    @Override
    public CompoundTag toTag(@Nonnull final CompoundTag tag) {
        final Registry<Enchantment> registry = Registry.ENCHANTMENT;

        for (final Enchantment enchantment : this) {
            final Integer level = this.get(enchantment);

            if (level != null) {
                final Identifier identifier = registry.getId(enchantment);

                if (identifier != null) {
                    tag.putInt(identifier.toString(), this.get(enchantment));
                }
            }
        }

        return tag;
    }
}
