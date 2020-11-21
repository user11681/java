package user11681.thorium.asm.mixin.client.gui.screen.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import user11681.thorium.asm.duck.client.gui.screen.ScreenAccess;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class ScreenMixin implements ScreenAccess {}
