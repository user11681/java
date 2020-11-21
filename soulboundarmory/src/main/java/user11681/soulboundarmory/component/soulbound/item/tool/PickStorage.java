package user11681.soulboundarmory.component.soulbound.item.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import user11681.soulboundarmory.client.i18n.Translations;
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
import user11681.usersmanual.text.StringifiedText;

import static net.minecraft.enchantment.Enchantments.UNBREAKING;
import static net.minecraft.enchantment.Enchantments.VANISHING_CURSE;
import static net.minecraft.entity.EquipmentSlot.MAINHAND;
import static net.minecraft.entity.attribute.EntityAttributeModifier.Operation.ADDITION;
import static user11681.soulboundarmory.component.statistics.Category.ATTRIBUTE;
import static user11681.soulboundarmory.component.statistics.Category.DATUM;
import static user11681.soulboundarmory.component.statistics.StatisticType.ATTRIBUTE_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.EFFICIENCY;
import static user11681.soulboundarmory.component.statistics.StatisticType.ENCHANTMENT_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.EXPERIENCE;
import static user11681.soulboundarmory.component.statistics.StatisticType.LEVEL;
import static user11681.soulboundarmory.component.statistics.StatisticType.MINING_LEVEL;
import static user11681.soulboundarmory.component.statistics.StatisticType.REACH;
import static user11681.soulboundarmory.component.statistics.StatisticType.SKILL_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.SPENT_ATTRIBUTE_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.SPENT_ENCHANTMENT_POINTS;

public class PickStorage extends ToolStorage<PickStorage> {
    public PickStorage(final SoulboundComponent component, final Item item) {
        super(component, item);

        this.statistics = Statistics.create()
                .category(DATUM, EXPERIENCE, LEVEL, SKILL_POINTS, ATTRIBUTE_POINTS, ENCHANTMENT_POINTS, SPENT_ATTRIBUTE_POINTS, SPENT_ENCHANTMENT_POINTS)
                .category(ATTRIBUTE, EFFICIENCY, REACH, MINING_LEVEL)
                .min(0.5, EFFICIENCY).min(2, REACH)
                .max(3, MINING_LEVEL)
                .build();
        this.enchantments = new EnchantmentStorage((final Enchantment enchantment) -> {
            final String name = enchantment.getName(1).getString().toLowerCase();

            return enchantment.isAcceptableItem(this.itemStack) && !CollectionUtil.hashSet(UNBREAKING, VANISHING_CURSE).contains(enchantment)
                    && !name.contains("soulbound") && !name.contains("holding") && !name.contains("smelt")
                    && !name.contains("mending");
        });
        this.skills = new SkillStorage(Skills.ENDER_PULL, Skills.AMBIDEXTERITY);
    }

    @Override
    public Item getConsumableItem() {
        return Items.WOODEN_PICKAXE;
    }

    @Override
    public Text getName() {
        return Translations.SOULBOUND_PICK;
    }

    @Override
    public StorageType<PickStorage> getType() {
        return StorageType.PICK;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(final EquipmentSlot slot) {
        final Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create();

        if (slot == MAINHAND) {
            modifiers.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(SoulboundArmoryAttributes.REACH_MODIFIER_UUID, "Tool modifier", this.getAttributeRelative(REACH), ADDITION));
        }

        return modifiers;
    }

    @Override
    public ArrayMap<Statistic, Text> getScreenAttributes() {
        final ArrayMap<Statistic, Text> entries = new OrderedArrayMap<>(3);

        entries.put(this.getStatistic(EFFICIENCY), new StringifiedText("%s%s: %s", Translations.TOOL_EFFICIENCY_FORMAT, Translations.TOOL_EFFICIENCY_NAME, this.formatStatistic(EFFICIENCY)));
        entries.put(this.getStatistic(MINING_LEVEL), new StringifiedText("%s%s: %s (%s)", Translations.MINING_LEVEL_FORMAT, Translations.MINING_LEVEL_NAME, this.formatStatistic(MINING_LEVEL), this.getMiningLevelName()));
        entries.put(this.getStatistic(REACH), new StringifiedText("%s%s: %s", Translations.REACH_FORMAT, Translations.ATTACK_RANGE_NAME, this.formatStatistic(REACH)));

        return entries;
    }

    @Override
    public List<Text> getTooltip() {
        final NumberFormat FORMAT = DecimalFormat.getInstance();
        final List<Text> tooltip = new ArrayList<>(5);

        tooltip.add(new LiteralText(String.format(" %s%s %s", Translations.REACH_FORMAT, FORMAT.format(this.getAttribute(REACH)), Translations.ATTACK_RANGE_NAME)));
        tooltip.add(new LiteralText(String.format(" %s%s %s", Translations.TOOL_EFFICIENCY_FORMAT, FORMAT.format(this.getAttribute(EFFICIENCY)), Translations.TOOL_EFFICIENCY_NAME)));
        tooltip.add(new LiteralText(String.format(" %s%s %s", Translations.MINING_LEVEL_FORMAT, FORMAT.format(this.getAttribute(MINING_LEVEL)), Translations.MINING_LEVEL_NAME)));

        tooltip.add(new LiteralText(""));
        tooltip.add(new LiteralText(""));

        return tooltip;
    }

    @Override
    public double getIncrease(final StatisticType statistic, final int points) {
        return statistic == EFFICIENCY
                ? 0.5
                : statistic == REACH
                ? 0.1
                : statistic == MINING_LEVEL
                ? 0.2
                : 0;
    }
}
