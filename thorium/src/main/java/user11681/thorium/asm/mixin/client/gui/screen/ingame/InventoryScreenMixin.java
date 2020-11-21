package user11681.thorium.asm.mixin.client.gui.screen.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import user11681.thorium.asm.duck.client.gui.screen.ScreenAccess;
import user11681.thorium.config.ThoriumConfiguration;

@Environment(EnvType.CLIENT)
@Mixin(value = InventoryScreen.class, priority = 5000)
public abstract class InventoryScreenMixin extends Screen {
    protected InventoryScreenMixin(final Text title) {
        super(title);
    }

    @Redirect(method = "init",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addButton(Lnet/minecraft/client/gui/widget/AbstractButtonWidget;)Lnet/minecraft/client/gui/widget/AbstractButtonWidget;"))
    public AbstractButtonWidget removeRecipeBook(final InventoryScreen screen, final AbstractButtonWidget button) {
        return ThoriumConfiguration.Data.Client.hideRecipeBook ? null : ((ScreenAccess) screen).addButton(button);
    }
}
