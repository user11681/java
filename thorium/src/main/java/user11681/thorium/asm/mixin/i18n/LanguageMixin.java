package user11681.thorium.asm.mixin.i18n;

import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import user11681.thorium.config.ThoriumConfiguration;

@Mixin(value = TranslationStorage.class, targets = "net.minecraft.util.Language$1")
public abstract class LanguageMixin {
    @Inject(method = "get(Ljava/lang/String;)Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
    public void get(final String key, final CallbackInfoReturnable<String> info) {
        if (ThoriumConfiguration.Data.brownSand) {
            if (!key.equals("color.minecraft.brown") && !key.contains("text.autoconfig.arbitrarypatches.option.brownSand.@Tooltip")) {
                final String value = info.getReturnValue();
                final String[] words = value.split(" ");
                String word;

                for (int i = 0, length = words.length; i < length; i++) {
                    word = words[i];

                    if (!word.toLowerCase().contains("soulbound")) {
                        final String soul = key.equals("block.minecraft.soul_sand") ? words[0] : Language.getInstance().get("block.minecraft.soul_sand").split(" ")[0];
                        final String brown = Language.getInstance().get("color.minecraft.brown");
                        final boolean upper;

                        if (word.contains(soul)) {
                            upper = true;
                        } else if (word.contains(soul.toLowerCase())) {
                            upper = false;
                        } else {
                            continue;
                        }

                        words[i] = word.replaceFirst(upper ? soul : soul.toLowerCase(), upper ? brown : brown.toLowerCase());
                    }
                }

                info.setReturnValue(String.join(" ", words));
            }
        }
    }
}
