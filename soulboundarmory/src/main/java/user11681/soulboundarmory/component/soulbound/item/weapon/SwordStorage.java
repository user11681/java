package user11681.soulboundarmory.component.soulbound.item.weapon;

import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
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
import user11681.usersmanual.entity.AttributeModifierOperations;
import user11681.usersmanual.text.StringifiedText;

import static net.minecraft.enchantment.Enchantments.UNBREAKING;
import static net.minecraft.enchantment.Enchantments.VANISHING_CURSE;
import static net.minecraft.entity.EquipmentSlot.MAINHAND;
import static user11681.soulboundarmory.SoulboundArmory.IMPACT;
import static user11681.soulboundarmory.component.statistics.Category.ATTRIBUTE;
import static user11681.soulboundarmory.component.statistics.Category.DATUM;
import static user11681.soulboundarmory.component.statistics.StatisticType.ATTACK_DAMAGE;
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

public class SwordStorage extends WeaponStorage<SwordStorage> {
    protected int lightningCooldown;

    public SwordStorage(final SoulboundComponent component, final Item item) {
        super(component, item);

        this.statistics = Statistics.create()
                .category(DATUM, EXPERIENCE, LEVEL, SKILL_POINTS, ATTRIBUTE_POINTS, ENCHANTMENT_POINTS, SPENT_ATTRIBUTE_POINTS, SPENT_ENCHANTMENT_POINTS)
                .category(ATTRIBUTE, ATTACK_SPEED, ATTACK_DAMAGE, CRITICAL_STRIKE_PROBABILITY, EFFICIENCY, REACH)
                .min(1.6, ATTACK_SPEED).min(4, ATTACK_DAMAGE).min(3, REACH)
                .max(1, CRITICAL_STRIKE_PROBABILITY)
                .build();
        this.enchantments = new EnchantmentStorage((final Enchantment enchantment) -> {
            final String name = enchantment.getName(1).asString().toLowerCase();

            return enchantment.isAcceptableItem(this.itemStack) && !CollectionUtil.hashSet(UNBREAKING, VANISHING_CURSE).contains(enchantment)
                    && (enchantment == IMPACT || !name.contains("soulbound")) && !name.contains("holding")
                    && !name.contains("mending");
        });
        this.skills = new SkillStorage(Skills.NOURISHMENT, Skills.SUMMON_LIGHTNING);
    }

    public static SwordStorage get(final Entity entity) {
        return Components.WEAPON_COMPONENT.get(entity).getStorage(StorageType.SWORD);
    }

    @Override
    public Text getName() {
        return Translations.SOULBOUND_SWORD;
    }

    @Override
    public StorageType<SwordStorage> getType() {
        return StorageType.SWORD;
    }

    public int getLightningCooldown() {
        return this.lightningCooldown;
    }

    public void resetLightningCooldown() {
        if (!this.player.isCreative()) {
            this.lightningCooldown = (int) Math.round(96 / this.getAttribute(ATTACK_SPEED));
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(final Multimap<EntityAttribute, EntityAttributeModifier> modifiers, final EquipmentSlot slot) {
        if (slot == MAINHAND) {
            final EntityAttributeModifier.Operation addition = EntityAttributeModifier.Operation.ADDITION;

            modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(AttributeModifierIdentifiers.ATTACK_DAMAGE_MODIFIER_ID,
                    "Weapon modifier", this.getAttributeRelative(ATTACK_DAMAGE), addition
            ));
            modifiers.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(AttributeModifierIdentifiers.ATTACK_SPEED_MODIFIER_ID,
                    "Weapon modifier", this.getAttributeRelative(ATTACK_SPEED), addition
            ));
            modifiers.put(SoulboundArmoryAttributes.GENERIC_EFFICIENCY, new EntityAttributeModifier(SoulboundArmoryAttributes.EFFICIENCY_MODIFIER_ID,
                    "Weapon modifier", this.getAttribute(EFFICIENCY), EntityAttributeModifier.Operation.MULTIPLY_TOTAL
            ));
            modifiers.put(SoulboundArmoryAttributes.GENERIC_CRITICAL_STRIKE_PROBABILITY, new EntityAttributeModifier(SoulboundArmoryAttributes.CRITICAL_STRIKE_PROBABILITY_MODIFIER_ID,
                    "Weapon modifier", this.getAttribute(CRITICAL_STRIKE_PROBABILITY), AttributeModifierOperations.PERCENTAGE_ADDITION
            ));
        }

        return modifiers;
    }

    @Override
    public ArrayMap<Statistic, Text> getScreenAttributes() {
        final ArrayMap<Statistic, Text> entries = new OrderedArrayMap<>();

        entries.put(this.getStatistic(ATTACK_SPEED), new StringifiedText("%s%s: %s", Translations.ATTACK_SPEED_FORMAT, Translations.ATTACK_SPEED_NAME, this.formatStatistic(ATTACK_SPEED)).formatted(Formatting.GOLD));
        entries.put(this.getStatistic(ATTACK_DAMAGE), new StringifiedText("%s%s: %s", Translations.ATTACK_DAMAGE_FORMAT, Translations.ATTACK_DAMAGE_NAME, this.formatStatistic(ATTACK_DAMAGE)));
        entries.put(this.getStatistic(CRITICAL_STRIKE_PROBABILITY), new StringifiedText("%s%s: %s%%", Translations.CRITICAL_STRIKE_PROBABILITY_FORMAT, Translations.CRITICAL_STRIKE_PROBABILITY_NAME, this.formatStatistic(CRITICAL_STRIKE_PROBABILITY)));
        entries.put(this.getStatistic(EFFICIENCY), new StringifiedText("%s%s: %s", Translations.WEAPON_EFFICIENCY_FORMAT, Translations.WEAPON_EFFICIENCY_NAME, this.formatStatistic(EFFICIENCY)));

        return entries;
    }

    @Override
    public List<Text> getTooltip() {
        final NumberFormat format = DecimalFormat.getInstance();
        final List<Text> tooltip = new ArrayList<>();

        tooltip.add(new LiteralText(String.format(" %s%s %s", Translations.ATTACK_SPEED_FORMAT, format.format(this.getAttribute(ATTACK_SPEED)), Translations.ATTACK_SPEED_NAME.toString())));
        tooltip.add(new LiteralText(String.format(" %s%s %s", Translations.ATTACK_DAMAGE_FORMAT, format.format(this.getAttributeTotal(ATTACK_DAMAGE)), Translations.ATTACK_DAMAGE_NAME.toString())));

        tooltip.add(new LiteralText(""));
        tooltip.add(new LiteralText(""));

        tooltip.add(new LiteralText(String.format(" %s%s%% %s", Translations.CRITICAL_STRIKE_PROBABILITY_FORMAT, format.format(this.getAttribute(CRITICAL_STRIKE_PROBABILITY) * 100), Translations.CRITICAL_STRIKE_PROBABILITY_NAME.toString())));
        tooltip.add(new LiteralText(String.format(" %s%s %s", Translations.TOOL_EFFICIENCY_FORMAT, format.format(this.getAttribute(EFFICIENCY)), Translations.TOOL_EFFICIENCY_NAME.toString())));

        return tooltip;
    }

    @Override
    public Item getConsumableItem() {
        return Items.WOODEN_SWORD;
    }

    @Override
    public double getIncrease(final StatisticType statistic, final int points) {
        return statistic == ATTACK_SPEED
                ? 0.03
                : statistic == ATTACK_DAMAGE
                ? 0.07
                : statistic == CRITICAL_STRIKE_PROBABILITY
                ? 0.015
                : statistic == EFFICIENCY
                ? 0.04
                : 0;
    }

    @Override
    public void tick() {
        if (!this.isClient) {
            if (this.lightningCooldown > 0) {
                this.lightningCooldown--;
            }
        }
    }

    @Override
    public void fromTag(@Nonnull final CompoundTag tag) {
        super.fromTag(tag);

        this.lightningCooldown = tag.getInt("lightningCooldown");
    }

    @Nonnull
    @Override
    public CompoundTag toTag(@Nonnull CompoundTag tag) {
        tag = super.toTag(tag);

        tag.putInt("lightningCooldown", this.getLightningCooldown());

        return tag;
    }
}
