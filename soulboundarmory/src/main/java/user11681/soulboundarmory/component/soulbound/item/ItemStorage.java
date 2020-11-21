package user11681.soulboundarmory.component.soulbound.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.util.NbtSerializable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import user11681.cell.client.gui.screen.ScreenTab;
import user11681.soulboundarmory.SoulboundArmory;
import user11681.soulboundarmory.SoulboundArmoryClient;
import user11681.soulboundarmory.client.gui.screen.tab.SoulboundTab;
import user11681.soulboundarmory.component.Components;
import user11681.soulboundarmory.component.soulbound.player.SoulboundComponent;
import user11681.soulboundarmory.component.statistics.Category;
import user11681.soulboundarmory.component.statistics.EnchantmentStorage;
import user11681.soulboundarmory.component.statistics.SkillStorage;
import user11681.soulboundarmory.component.statistics.Statistic;
import user11681.soulboundarmory.component.statistics.StatisticType;
import user11681.soulboundarmory.component.statistics.Statistics;
import user11681.soulboundarmory.config.Configuration;
import user11681.soulboundarmory.item.SoulboundItem;
import user11681.soulboundarmory.network.common.ExtendedPacketBuffer;
import user11681.soulboundarmory.registry.Packets;
import user11681.soulboundarmory.registry.Registries;
import user11681.soulboundarmory.skill.Skill;
import user11681.soulboundarmory.skill.SkillContainer;
import user11681.usersmanual.client.gui.screen.ScreenTab;
import user11681.usersmanual.collections.ArrayMap;
import user11681.usersmanual.item.ItemUtil;

import static user11681.soulboundarmory.SoulboundArmoryClient.CLIENT;
import static user11681.soulboundarmory.component.statistics.Category.ATTRIBUTE;
import static user11681.soulboundarmory.component.statistics.Category.DATUM;
import static user11681.soulboundarmory.component.statistics.Category.ENCHANTMENT;
import static user11681.soulboundarmory.component.statistics.Category.SKILL;
import static user11681.soulboundarmory.component.statistics.StatisticType.ATTACK_DAMAGE;
import static user11681.soulboundarmory.component.statistics.StatisticType.ATTACK_SPEED;
import static user11681.soulboundarmory.component.statistics.StatisticType.ATTRIBUTE_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.CRITICAL_STRIKE_PROBABILITY;
import static user11681.soulboundarmory.component.statistics.StatisticType.ENCHANTMENT_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.EXPERIENCE;
import static user11681.soulboundarmory.component.statistics.StatisticType.LEVEL;
import static user11681.soulboundarmory.component.statistics.StatisticType.REACH;
import static user11681.soulboundarmory.component.statistics.StatisticType.SKILL_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.SPENT_ATTRIBUTE_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.SPENT_ENCHANTMENT_POINTS;

public abstract class ItemStorage<T extends ItemStorage<T>> implements NbtSerializable {
    protected static final NumberFormat FORMAT = DecimalFormat.getInstance();

    protected final SoulboundComponent component;
    protected final PlayerEntity player;
    protected final boolean isClient;
    protected final Item item;

    protected EnchantmentStorage enchantments;
    protected SkillStorage skills;
    protected Statistics statistics;
    protected ItemStack itemStack;
    protected boolean unlocked;
    protected int boundSlot;
    protected int currentTab;

    public ItemStorage(final SoulboundComponent component, final Item item) {
        this.component = component;
        this.player = component.getEntity();
        this.isClient = component.getEntity().world.isClient;
        this.item = item;
        this.itemStack = this.newItemStack();
    }

    public static ItemStorage<?> get(final Entity entity, final Item item) {
        for (final SoulboundComponent component : Components.getComponents(entity)) {
            for (final ItemStorage<?> storage : component.getStorages().values()) {
                if (storage.player == entity && storage.getItem() == item) {
                    return storage;
                }
            }
        }

        return null;
    }

    public static ItemStorage<?> get(final Entity entity, final StorageType<?> type) {
        for (final ComponentType<? extends SoulboundComponent> component : Components.SOULBOUND_COMPONENTS) {
            for (final ItemStorage<?> storage : component.get(entity).getStorages().values()) {
                if (storage.getType() == type) {
                    return storage;
                }
            }
        }

        return null;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public SoulboundComponent getComponent() {
        return this.component;
    }

    public Item getItem() {
        return this.item;
    }

    public ItemStack getMenuEquippedStack() {
        for (final ItemStack itemStack : this.player.getItemsHand()) {
            final Item item = itemStack.getItem();

            if (item == this.getItem() || item == this.getConsumableItem()) {
                return itemStack;
            }
        }

        return null;
    }

    public boolean isUnlocked() {
        return this.unlocked;
    }

    public void setUnlocked(final boolean unlocked) {
        this.unlocked = unlocked;
    }

    public int size(final Category category) {
        return this.statistics.size(category);
    }

    public Statistic getStatistic(final StatisticType statistic) {
        return this.statistics.get(statistic);
    }

    public Statistic getStatistic(final Category category, final StatisticType statistic) {
        return this.statistics.get(category, statistic);
    }

    public int getDatum(final StatisticType statistic) {
        return this.statistics.get(statistic).intValue();
    }

    public double getAttributeTotal(StatisticType statistic) {
        if (statistic == ATTACK_DAMAGE) {
            double attackDamage = this.getAttribute(ATTACK_DAMAGE);

            for (final Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
                final Enchantment enchantment = entry.getKey();

                if (entry.getValue() > 0) {
                    attackDamage += enchantment.getAttackDamage(this.getEnchantment(enchantment), EntityGroup.DEFAULT);
                }
            }

            return attackDamage;
        }

        return this.getAttribute(statistic);
    }

    public double getAttributeRelative(final StatisticType attribute) {
        if (attribute == ATTACK_SPEED) {
            return this.getAttribute(ATTACK_SPEED) - 4;
        }

        if (attribute == ATTACK_DAMAGE) {
            return this.getAttribute(ATTACK_DAMAGE) - 1;
        }

        if (attribute == REACH) {
            return this.getAttribute(REACH) - 3;
        }

        return this.getAttribute(attribute);
    }

    public double getAttribute(final StatisticType type) {
        return this.statistics.get(type).doubleValue();
    }

    public boolean incrementStatistic(final StatisticType type, final double amount) {
        boolean leveledUp = false;

        if (type == EXPERIENCE) {
            final Statistic statistic = this.getStatistic(EXPERIENCE);
            statistic.add(amount);
            final int xp = statistic.intValue();

            if (xp >= this.getNextLevelXP() && this.canLevelUp()) {
                final int nextLevelXP = this.getNextLevelXP();

                this.incrementStatistic(LEVEL, 1);
                this.incrementStatistic(EXPERIENCE, -nextLevelXP);

                leveledUp = true;
            }

            if (xp < 0) {
                final int currentLevelXP = this.getLevelXP(this.getDatum(LEVEL) - 1);

                this.incrementStatistic(LEVEL, -1);
                this.incrementStatistic(EXPERIENCE, currentLevelXP);
            }
        } else if (type == LEVEL) {
            final int sign = (int) Math.signum(amount);

            for (int i = 0; i < Math.abs(amount); i++) {
                this.onLevelup(sign);
            }
        } else {
            this.statistics.add(type, amount);
        }

        this.updateItemStack();
        this.sync();

        return leveledUp;
    }

    public void incrementPoints(final StatisticType type, final int points) {
        final int sign = (int) Math.signum(points);
        final Statistic statistic = this.getStatistic(type);

        for (int i = 0; i < Math.abs(points); i++) {
            if (sign > 0 && this.getDatum(ATTRIBUTE_POINTS) > 0 || sign < 0 && statistic.isAboveMin()) {
                this.getStatistic(ATTRIBUTE_POINTS).add(-sign);
                this.getStatistic(SPENT_ATTRIBUTE_POINTS).add(sign);

                final double change = sign * this.getIncrease(type);

                statistic.add(change);
                statistic.incrementPoints();
            }
        }

        this.updateItemStack();
        this.sync();
    }

    public double getIncrease(final StatisticType statisticType) {
        final Statistic statistic = this.getStatistic(statisticType);

        return statistic == null ? 0 : this.getIncrease(statisticType, statistic.getPoints());
    }

    public void setStatistic(final StatisticType statistic, final Number value) {
        this.statistics.put(statistic, value);

        this.sync();
    }

    public boolean canLevelUp() {
        final Configuration configuration = Configuration.instance();

        return this.getDatum(LEVEL) < configuration.maxLevel || configuration.maxLevel < 0;
    }

    public void onLevelup(final int sign) {
        final Statistic statistic = this.getStatistic(LEVEL);
        statistic.add(sign);
        final int level = statistic.intValue();
        final Configuration configuration = Configuration.instance();

        if (level % configuration.levelsPerEnchantment == 0) {
            this.incrementStatistic(ENCHANTMENT_POINTS, sign);
        }

        if (level % configuration.levelsPerSkillPoint == 0) {
            this.incrementStatistic(SKILL_POINTS, sign);
        }

        this.incrementStatistic(ATTRIBUTE_POINTS, sign);
    }

    public List<SkillContainer> getSkills() {
        final List<SkillContainer> skills = this.skills.values();

        skills.sort(Comparator.comparingInt(SkillContainer::getTier));

        return skills;
    }

    public SkillContainer getSkill(final Identifier identifier) {
        return this.getSkill(Registries.SKILL.get(identifier));
    }

    public SkillContainer getSkill(final Skill skill) {
        return this.skills.get(skill);
    }

    public boolean hasSkill(final Identifier identifier) {
        return this.hasSkill(Registries.SKILL.get(identifier));
    }

    public boolean hasSkill(final Skill skill) {
        return this.skills.contains(skill);
    }

    public boolean hasSkill(final Skill skill, final int level) {
        return this.skills.contains(skill, level);
    }

    public void upgradeSkill(final SkillContainer skill) {
//        if (this.isClient) {
//            MainClient.PACKET_REGISTRY.sendToServer(Packets.C2S_SKILL, new ExtendedPacketBuffer(this, item).writeString(skill.toString()));
//        } else {
        final int points = this.getDatum(SKILL_POINTS);
        final int cost = skill.getCost();

        if (skill.canBeLearned(points)) {
            skill.learn();

            this.incrementStatistic(SKILL_POINTS, -cost);
        } else if (skill.canBeUpgraded(points)) {
            skill.upgrade();

            this.incrementStatistic(SKILL_POINTS, -cost);
        }
//        }
    }

    public int getNextLevelXP() {
        return this.getLevelXP(this.getDatum(LEVEL));
    }

    public int getEnchantment(final Enchantment enchantment) {
        return this.enchantments.get(enchantment);
    }

    public EnchantmentStorage getEnchantments() {
        return this.enchantments;
    }

    public void addEnchantment(final Enchantment enchantment, final int value) {
        final int current = this.getEnchantment(enchantment);
        final int change = Math.max(0, current + value) - current;

        this.statistics.add(ENCHANTMENT_POINTS, -change);
        this.statistics.add(SPENT_ENCHANTMENT_POINTS, change);

        this.enchantments.add(enchantment, change);
        this.updateItemStack();

        this.sync();
    }

    public void reset() {
        for (final Category category : Registries.CATEGORY) {
            this.reset(category);
        }

        this.unlocked = false;
        this.boundSlot = -1;
        this.currentTab = 0;

        this.sync();
    }

    public void reset(final Category category) {
        if (category == DATUM) {
            this.statistics.reset(DATUM);
        } else if (category == ATTRIBUTE) {
            for (final Statistic type : this.statistics.get(ATTRIBUTE).values()) {
                this.incrementStatistic(ATTRIBUTE_POINTS, type.getPoints());
                type.reset();
            }

            this.setStatistic(SPENT_ATTRIBUTE_POINTS, 0);
            this.updateItemStack();
        } else if (category == ENCHANTMENT) {
            for (final Enchantment enchantment : this.enchantments) {
                this.incrementStatistic(ENCHANTMENT_POINTS, this.enchantments.get(enchantment));
                this.enchantments.put(enchantment, 0);
            }

            this.getStatistic(SPENT_ENCHANTMENT_POINTS).setToMin();
            this.updateItemStack();
        } else if (category == SKILL) {
            this.skills.reset();

            this.getStatistic(SKILL_POINTS).setToMin();
        }

        this.sync();
    }

    public boolean canUnlock() {
        return !this.unlocked && ItemUtil.isItemEquipped(this.player, this.getConsumableItem());
    }

    public boolean canConsume(final Item item) {
        return this.getConsumableItem() == item;
    }

    public int getBoundSlot() {
        return this.boundSlot;
    }

    public void bindSlot(final int boundSlot) {
        this.boundSlot = boundSlot;
    }

    public void unbindSlot() {
        this.boundSlot = -1;
    }

    public int getCurrentTab() {
        return this.currentTab;
    }

    public void setCurrentTab(final int tab) {
        this.currentTab = tab;

        if (this.isClient) {
            this.sync();
        }
    }

    @SuppressWarnings("VariableUseSideOnly")
    public void refresh() {
        if (this.isClient) {
            if (CLIENT.currentScreen instanceof SoulboundTab) {
                final List<Item> handItems = ItemUtil.getHandItems(this.player);

                if (handItems.contains(this.getItem())) {
                    this.openGUI();
                } else if (handItems.contains(this.getConsumableItem())) {
                    this.openGUI(0);
                }
            }
        } else {
            SoulboundArmory.PACKET_REGISTRY.sendToPlayer(this.player, Packets.S2C_REFRESH, new ExtendedPacketBuffer(this));
        }
    }

    public void openGUI() {
        this.openGUI(ItemUtil.getEquippedItemStack(this.player.inventory, this.getBaseItemClass()) == null ? 0 : this.currentTab);
    }

    @SuppressWarnings({"LocalVariableDeclarationSideOnly", "VariableUseSideOnly", "MethodCallSideOnly"})
    public void openGUI(final int tab) {
        if (this.isClient) {
            final Screen currentScreen = CLIENT.currentScreen;

            if (currentScreen instanceof SoulboundTab && this.currentTab == tab) {
                ((SoulboundTab) currentScreen).refresh();
            } else {
                final List<ScreenTab> tabs = this.getTabs();

                CLIENT.openScreen(tabs.get(MathHelper.clamp(tab, 0, tabs.size() - 1)));
            }
        } else {
            SoulboundArmory.PACKET_REGISTRY.sendToPlayer(this.player, Packets.S2C_OPEN_GUI, new ExtendedPacketBuffer(this).writeInt(tab));
        }
    }

    public boolean isItemEquipped() {
        return ItemUtil.isItemEquipped(this.player, this.getItem());
    }

    public boolean isAnyItemEquipped() {
        return this.getMenuEquippedStack() != null;
    }

    public void removeOtherItems() {
        for (final ItemStorage<?> storage : this.component.getStorages().values()) {
            if (storage != this) {
                final PlayerEntity player = this.player;

                for (final ItemStack itemStack : ItemUtil.getCombinedSingleInventory(player)) {
                    if (itemStack.getItem() == storage.getItem()) {
                        player.inventory.removeOne(itemStack);
                    }
                }
            }
        }
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    protected void updateItemStack() {
        final ItemStack itemStack = this.itemStack = this.newItemStack();

        for (final EquipmentSlot slot : EquipmentSlot.values()) {
            final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = this.getAttributeModifiers(slot);

            for (final EntityAttribute attribute : attributeModifiers.keySet()) {
                for (final EntityAttributeModifier modifier : attributeModifiers.get(attribute)) {
                    itemStack.addAttributeModifier(attribute, modifier, EquipmentSlot.MAINHAND);
                }
            }
        }

        for (final Entry<Enchantment, Integer> entry : this.enchantments.entrySet()) {
            final int level = entry.getValue();

            if (level > 0) {
                itemStack.addEnchantment(entry.getKey(), level);
            }
        }
    }

    protected ItemStack newItemStack() {
        final ItemStack itemStack = new ItemStack(this.item);

        Components.ITEM_DATA.get(itemStack).storage = this;

        return itemStack;
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(final EquipmentSlot slot) {
        return this.getAttributeModifiers(LinkedHashMultimap.create(), slot);
    }

    protected String formatStatistic(final StatisticType statistic) {
        final double value = this.getAttributeTotal(statistic);

        return FORMAT.format(statistic == CRITICAL_STRIKE_PROBABILITY ? value * 100 : value);
    }


    @Override
    public void fromTag(@Nonnull final CompoundTag tag) {
        this.statistics.fromTag(tag.getCompound("statistics"));
        this.enchantments.fromTag(tag.getCompound("enchantments"));
        this.skills.fromTag(tag.getCompound("skills"));
        this.setUnlocked(tag.getBoolean("unlocked"));
        this.bindSlot(tag.getInt("slot"));
        this.setCurrentTab(tag.getInt("tab"));

        this.updateItemStack();
    }

    @Nonnull
    @Override
    public CompoundTag toTag(@Nonnull final CompoundTag tag) {
        tag.put("statistics", this.statistics.toTag(new CompoundTag()));
        tag.put("enchantments", this.enchantments.toTag(new CompoundTag()));
        tag.put("skills", this.skills.toTag(new CompoundTag()));
        tag.putBoolean("unlocked", this.unlocked);
        tag.putInt("slot", this.getBoundSlot());
        tag.putInt("tab", this.getCurrentTab());

        return tag;
    }

    public CompoundTag toClientTag() {
        final CompoundTag tag = new CompoundTag();

        tag.putInt("tab", this.currentTab);

        return tag;
    }

    @SuppressWarnings("VariableUseSideOnly")
    public void sync() {
        if (!this.isClient) {
            SoulboundArmory.PACKET_REGISTRY.sendToPlayer(this.player, Packets.S2C_SYNC, new ExtendedPacketBuffer(this).writeCompoundTag(this.toTag(new CompoundTag())));
        } else {
            SoulboundArmoryClient.PACKET_REGISTRY.sendToServer(Packets.C2S_SYNC, new ExtendedPacketBuffer(this).writeCompoundTag(this.toClientTag()));
        }
    }

    public void tick() {

    }

    public abstract Text getName();

    public abstract ArrayMap<Statistic, Text> getScreenAttributes();

    public abstract List<Text> getTooltip();

    public abstract Item getConsumableItem();

    public abstract double getIncrease(StatisticType statistic, int points);

    public abstract int getLevelXP(int level);

    @Environment(EnvType.CLIENT)
    public abstract List<ScreenTab> getTabs();

    public abstract StorageType<T> getType();

    public abstract Class<? extends SoulboundItem> getBaseItemClass();

    public abstract Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(final Multimap<EntityAttribute, EntityAttributeModifier> modifiers, final EquipmentSlot slot);
}
