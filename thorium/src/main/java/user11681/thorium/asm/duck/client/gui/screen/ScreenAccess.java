package user11681.thorium.asm.duck.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.AbstractButtonWidget;

@Environment(EnvType.CLIENT)
public interface ScreenAccess {
    <T extends AbstractButtonWidget> T addButton(final T button);
}
