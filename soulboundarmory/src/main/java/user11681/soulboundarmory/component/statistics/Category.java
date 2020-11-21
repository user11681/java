package user11681.soulboundarmory.component.statistics;

import net.minecraft.util.Identifier;
import user11681.soulboundarmory.SoulboundArmory;
import user11681.soulboundarmory.registry.Registries;
import user11681.usersmanual.registry.AbstractRegistryEntry;

public class Category extends AbstractRegistryEntry {
    public static final Category DATUM = Registries.CATEGORY.register(new Category(new Identifier(SoulboundArmory.ID, "datum")));
    public static final Category ATTRIBUTE = Registries.CATEGORY.register(new Category(new Identifier(SoulboundArmory.ID, "attribute")));
    public static final Category ENCHANTMENT = Registries.CATEGORY.register(new Category(new Identifier(SoulboundArmory.ID, "enchantment")));
    public static final Category SKILL = Registries.CATEGORY.register(new Category(new Identifier(SoulboundArmory.ID, "skill")));

    public Category(final Identifier identifier) {
        super(identifier);
    }
}
