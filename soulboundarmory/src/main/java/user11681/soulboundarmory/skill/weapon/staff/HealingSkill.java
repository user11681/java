package user11681.soulboundarmory.skill.weapon.staff;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import user11681.soulboundarmory.skill.Skill;
public class HealingSkill extends Skill {
    public HealingSkill(final Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getCost(final boolean learned, final int level) {
            return 2;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(final SpunScreen screen, final MatrixStack matrices, final int level, final int x, final int y, final int zOffset) {
        screen.renderGuiItem(PotionUtil.setPotion(Items.POTION.getStackForRender(), Potions.HEALING), x, y, zOffset);
    }
}
