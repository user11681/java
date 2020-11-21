package user11681.thorium.asm.mixin.client.gui.screen.options;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.options.SoundOptionsScreen;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import user11681.thorium.config.ThoriumConfiguration;

@Environment(EnvType.CLIENT)
@Mixin(SoundOptionsScreen.class)
public abstract class SoundOptionsScreenMixin {
    @ModifyVariable(method = "init", at = @At(value = "LOAD"), name = "soundCategory")
    public SoundCategory getCategory(final SoundCategory category) {
        if (ThoriumConfiguration.Data.Client.disableMusic && category == SoundCategory.MUSIC) {
            return SoundCategory.MASTER;
        }

        return category;
    }
}
