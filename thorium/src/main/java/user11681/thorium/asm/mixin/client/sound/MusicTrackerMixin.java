package user11681.thorium.asm.mixin.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MusicTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import user11681.thorium.config.ThoriumConfiguration;

@Environment(EnvType.CLIENT)
@Mixin(MusicTracker.class)
public class MusicTrackerMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(final CallbackInfo info) {
        if (ThoriumConfiguration.Data.Client.disableMusic) {
            info.cancel();
        }
    }
}
