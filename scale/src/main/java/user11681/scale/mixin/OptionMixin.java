package user11681.scale.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import user11681.scale.ScaleConfig;

@Environment(EnvType.CLIENT)
@Mixin(Option.class)
public abstract class OptionMixin {
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void readConfig(final CallbackInfo info) throws Throwable {
        ScaleConfig.INSTANCE.read();
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "method_18548", at = @At("RETURN"))
    private static void fixGuiScale(final GameOptions options, final Integer change, final CallbackInfo info) throws Throwable {
        if (ScaleConfig.INSTANCE.enabled && options.guiScale == 1) {
            options.guiScale = 2;

            ScaleConfig.INSTANCE.enabled = false;
            ScaleConfig.INSTANCE.write();
        }
    }
}
