package user11681.soulboundarmory.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import javax.annotation.Nonnull;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.fabric.impl.tool.attribute.ToolManagerImpl;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import user11681.soulboundarmory.client.i18n.Translations;
import user11681.soulboundarmory.component.Components;
import user11681.soulboundarmory.component.soulbound.item.StorageType;
import user11681.soulboundarmory.component.soulbound.item.tool.PickStorage;
import user11681.usersmanual.math.MathUtil;

import static user11681.soulboundarmory.component.statistics.StatisticType.EXPERIENCE;
import static user11681.soulboundarmory.component.statistics.StatisticType.LEVEL;
import static user11681.soulboundarmory.item.ModToolMaterials.SOULBOUND;

public class SoulboundPickItem extends PickaxeItem implements SoulboundToolItem {
    public SoulboundPickItem() {
        super(SOULBOUND, 0, -2.4F, new Settings());
    }

    @Override
    public boolean postMine(final ItemStack stack, final World world, final BlockState state, final BlockPos pos, final LivingEntity miner) {
        if (miner instanceof PlayerEntity && this.canMine(state, world, pos, (PlayerEntity) miner)) {
            final PickStorage component = StorageType.PICK.get(miner);
            final ToolManagerImpl.Entry entry = ToolManagerImpl.entryNullable(state.getBlock());
            int xp = MathUtil.roundRandomly(Math.min(state.getHardness(world, pos), 5), world.random);

            if (entry != null) {
                xp += entry.getMiningLevel(FabricToolTags.PICKAXES);
            }

            if (!world.isClient && component.incrementStatistic(EXPERIENCE, xp) && Components.CONFIG_COMPONENT.get(miner).getLevelupNotifications()) {
                ((PlayerEntity) miner).sendMessage(new TranslatableText(Translations.MESSAGE_LEVEL_UP.getKey(), stack.getName(), component.getDatum(LEVEL)), true);
            }
        }

        return true;
    }

    @Override
    @Nonnull
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(@Nonnull final EquipmentSlot slot) {
        return HashMultimap.create();
    }
}
