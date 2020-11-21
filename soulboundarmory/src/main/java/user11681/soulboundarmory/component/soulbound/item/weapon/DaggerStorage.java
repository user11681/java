package user11681.soulboundarmory.component.soulbound.item.weapon;

import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import user11681.soulboundarmory.client.i18n.Translations;
import user11681.soulboundarmory.component.Components;
import user11681.soulboundarmory.component.soulbound.item.StorageType;
import user11681.soulboundarmory.component.soulbound.player.SoulboundComponent;
import user11681.soulboundarmory.component.statistics.EnchantmentStorage;
import user11681.soulboundarmory.component.statistics.SkillStorage;
import user11681.soulboundarmory.component.statistics.Statistic;
import user11681.soulboundarmory.component.statistics.StatisticType;
import user11681.soulboundarmory.component.statistics.Statistics;
import user11681.soulboundarmory.entity.SoulboundArmoryAttributes;
import user11681.soulboundarmory.registry.Skills;
import user11681.usersmanual.collections.ArrayMap;
import user11681.usersmanual.collections.CollectionUtil;
import user11681.usersmanual.collections.OrderedArrayMap;
import user11681.usersmanual.entity.AttributeModifierIdentifiers;

import static net.minecraft.enchantment.Enchantments.UNBREAKING;
import static net.minecraft.enchantment.Enchantments.VANISHING_CURSE;
import static net.minecraft.entity.EquipmentSlot.MAINHAND;
import static net.minecraft.entity.attribute.EntityAttributeModifier.Operation.ADDITION;
import static user11681.soulboundarmory.SoulboundArmory.IMPACT;
import static user11681.soulboundarmory.component.statistics.Category.ATTRIBUTE;
import static user11681.soulboundarmory.component.statistics.Category.DATUM;
import static user11681.soulboundarmory.component.statistics.StatisticType.ATTACK_DAMAGE;
import static user11681.soulboundarmory.component.statistics.StatisticType.ATTACK_RANGE;
import static user11681.soulboundarmory.component.statistics.StatisticType.ATTACK_SPEED;
import static user11681.soulboundarmory.component.statistics.StatisticType.ATTRIBUTE_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.CRITICAL_STRIKE_PROBABILITY;
import static user11681.soulboundarmory.component.statistics.StatisticType.EFFICIENCY;
import static user11681.soulboundarmory.component.statistics.StatisticType.ENCHANTMENT_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.EXPERIENCE;
import static user11681.soulboundarmory.component.statistics.StatisticType.LEVEL;
import static user11681.soulboundarmory.component.statistics.StatisticType.REACH;
import static user11681.soulboundarmory.component.statistics.StatisticType.SKILL_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.SPENT_ATTRIBUTE_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.SPENT_ENCHANTMENT_POINTS;

public class DaggerStorage extends WeaponStorage<DaggerStorage> {
    public DaggerStorage(final SoulboundComponent component, final Item item) {
        super(component, item);

        this.statistics = Statistics.create()
                .category(DATUM, EXPERIENCE, LEVEL, SKILL_POINTS, ATTRIBUTE_POINTS, ENCHANTMENT_POINTS, SPENT_ATTRIBUTE_POINTS, SPENT_ENCHANTMENT_POINTS)
                .category(ATTRIBUTE, ATTACK_SPEED, ATTACK_DAMAGE, CRITICAL_STRIKE_PROBABILITY, EFFICIENCY, ATTACK_RANGE, REACH)
                .min(2, ATTACK_SPEED, ATTACK_DAMAGE, REACH)
                .max(1, CRITICAL_STRIKE_PROBABILITY).build();
        this.enchantments = new EnchantmentStorage((final Enchantment enchantment) -> {
            final String name = enchantment.getName(1).asString().toLowerCase();

            return enchantment.isAcceptableItem(this.itemStack) && !CollectionUtil.hashSet(UNBREAKING, VANISHING_CURSE).contains(enchantment)
                    && (enchantment == IMPACT || !name.contains("soulbound")) && !name.contains("holding")
                    && !name.contains("mending");
        });
        this.skills = new SkillStorage(Skills.NOURISHMENT, Skills.THROWING, Skills.SHADOW_CLONE, Skills.RETURN, Skills.SNEAK_RETURN);
    }

    public static DaggerStorage get(final Entity entity) {
        return Components.WEAPON_COMPONENT.get(entity).getStorage(StorageType.DAGGER);
    }

    @Override
    public Text getName() {
        return Translations.SOULBOUND_DAGGER;
    }

    @Override
    public StorageType<DaggerStorage> getType() {
        return StorageType.DAGGER;
    }

    @Override
    public Item getConsumableItem() {
        return Items.STONE_SWORD;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(final Multimap<EntityAttribute, EntityAttributeModifier> modifiers, final EquipmentSlot slot) {
        if (slot == MAINHAND) {
            modifiers.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(AttributeModifierIdentifiers.ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", this.getAttributeRelative(ATTACK_SPEED), ADDITION));
            modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(AttributeModifierIdentifiers.ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.getAttributeRelative(ATTACK_DAMAGE), ADDITION));
            modifiers.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(SoulboundArmoryAttributes.ATTACK_RANGE_MODIFIER_UUID, "Weapon modifier", this.getAttributeRelative(ATTACK_RANGE), ADDITION));
            modifiers.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(SoulboundArmoryAttributes.REACH_MODIFIER_UUID, "Tool modifier", this.getAttributeRelative(REACH), ADDITION));
        }

        return modifiers;
    }

    @Override
    public ArrayMap<Statistic, Text> getScreenAttributes() {
        final ArrayMap<Statistic, Text> entries = new OrderedArrayMap<>();
        final Statistic critical = this.getStatistic(CRITICAL_STRIKE_PROBABILITY);

        entries.put(this.getStatistic(ATTACK_SPEED), new TranslatableText("%s%s: %s", Translations.ATTACK_SPEED_FORMAT, Translations.ATTACK_SPEED_NAME, this.formatStatistic(ATTACK_SPEED)));
        entries.put(this.getStatistic(ATTACK_DAMAGE), new TranslatableText("%s%s: %s", Translations.ATTACK_DAMAGE_FORMAT, Translations.ATTACK_DAMAGE_NAME, this.formatStatistic(ATTACK_DAMAGE)));
        entries.put(critical, new TranslatableText("%s%s: %s%%", Translations.CRITICAL_STRIKE_PROBABILITY_FORMAT, Translations.CRITICAL_STRIKE_PROBABILITY_NAME, FORMAT.format(critical.doubleValue() * 100)));
        entries.put(this.getStatistic(EFFICIENCY), new TranslatableText("%s%s: %s", Translations.WEAPON_EFFICIENCY_FORMAT, Translations.WEAPON_EFFICIENCY_NAME, this.formatStatistic(EFFICIENCY)));

        return entries;
    }

    @Override
    public List<Text> getTooltip() {
        final NumberFormat format = DecimalFormat.getInstance();
        final List<Text> tooltip = new ArrayList<>();

        final TranslatableText text = new TranslatableText(" %s%s %s", format.format(this.getAttribute(ATTACK_SPEED)), Translations.ATTACK_SPEED_NAME);
        text.styled((final Style style) -> style.withColor(Formatting.byCode(Translations.ATTACK_SPEED_FORMAT.getKey().charAt(1))));
        tooltip.add(text);
        tooltip.add(new TranslatableText(" %s%s %s", Translations.ATTACK_DAMAGE_FORMAT, format.format(this.getAttributeTotal(ATTACK_DAMAGE)), Translations.ATTACK_DAMAGE_NAME));
        tooltip.add(new TranslatableText(""));
        tooltip.add(new TranslatableText(""));

        if (this.getAttribute(CRITICAL_STRIKE_PROBABILITY) > 0) {
            tooltip.add(new LiteralText(String.format(" %s%s%% %s", Translations.CRITICAL_STRIKE_PROBABILITY_FORMAT, format.format(this.getAttribute(CRITICAL_STRIKE_PROBABILITY) * 100), Translations.CRITICAL_STRIKE_PROBABILITY_NAME)));
        }

        if (this.getAttribute(EFFICIENCY) > 0) {
            tooltip.add(new LiteralText(String.format(" %s%s %s", Translations.TOOL_EFFICIENCY_FORMAT, format.format(this.getAttribute(EFFICIENCY)), Translations.TOOL_EFFICIENCY_NAME)));
        }

        return tooltip;
    }

    @Override
    public double getIncrease(final StatisticType statistic, final int points) {
        return statistic == ATTACK_SPEED
                ? 0.04
                : statistic == ATTACK_DAMAGE
                ? 0.05
                : statistic == CRITICAL_STRIKE_PROBABILITY
                ? 0.02
                : statistic == EFFICIENCY
                ? 0.06
                : 0;
    }
}
