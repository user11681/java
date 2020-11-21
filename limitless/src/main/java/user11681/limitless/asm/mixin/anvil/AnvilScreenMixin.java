package user11681.limitless.asm.mixin.anvil;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import user11681.limitless.config.LimitlessConfiguration;
import user11681.limitless.config.anvil.entry.AnvilNormalizationEntry;
import user11681.limitless.config.common.CostDisplay;
import user11681.limitless.enchantment.ExperienceUtil;

@Mixin(AnvilScreen.class)
abstract class AnvilScreenMixin extends HandledScreen<AnvilScreenHandler> {
    public AnvilScreenMixin(final AnvilScreenHandler handler, final PlayerInventory inventory, final Text title) {
        super(handler, inventory, title);
    }

    @ModifyConstant(method = "drawForeground",
                    constant = @Constant(intValue = 40))
    public int modifyLimit(final int limit) {
        return LimitlessConfiguration.instance.anvil.levelLimit;
    }

    @ModifyArg(method = "drawForeground",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/text/TranslatableText;<init>(Ljava/lang/String;[Ljava/lang/Object;)V"),
               index = 1)
    public Object[] showNormalizedCost(final Object[] arguments) {
        final int cost = this.handler.getLevelCost();
        final PlayerEntity player = this.playerInventory.player;
        final AnvilNormalizationEntry normalization = LimitlessConfiguration.instance.anvil.normalization;

        if (normalization.enabled && normalization.display != CostDisplay.NORMAL && !player.getAbilities().creativeMode && player.experienceLevel > cost) {
            final int relative = ExperienceUtil.normalizedCost(player, cost);

            if (normalization.display == CostDisplay.REPLACE) {
                arguments[0] = Integer.toString(relative);
            } else {
                arguments[0] += String.format(" (%s)", relative);
            }
        }

        return arguments;
    }
}
