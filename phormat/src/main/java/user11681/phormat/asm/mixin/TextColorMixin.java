package user11681.phormat.asm.mixin;

import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import user11681.phormat.api.ColorFunction;
import user11681.phormat.asm.access.FormattingAccess;

@Mixin(TextColor.class)
abstract class TextColorMixin {
    @Shadow
    @Final
    private int rgb;

    @SuppressWarnings("FieldMayBeFinal") // mutated via ASM
    private int phormat_previousColor = this.rgb;

    private boolean phormat_hasColorFunction;

    @SuppressWarnings({"unused", "RedundantSuppression"})
    private ColorFunction phormat_colorFunction;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "fromFormatting", at = @At("RETURN"), cancellable = true)
    private static void setColorFunction(final Formatting formatting, final CallbackInfoReturnable<TextColor> info) {
        final TextColorMixin color = (TextColorMixin) (Object) info.getReturnValue();

        if (color != null) {
            final FormattingAccess formattingAccess = (FormattingAccess) (Object) formatting;

            if (formattingAccess.isCustom()) {
                final ColorFunction colorFunction = formattingAccess.getColorFunction();

                if (colorFunction != null) {
                    color.phormat_colorFunction = colorFunction;
                    color.phormat_hasColorFunction = true;
                }
            }
        }
    }

    @Inject(method = "getHexCode", at = @At("HEAD"), cancellable = true)
    public void matchHexCode(final CallbackInfoReturnable<String> info) {
        if (this.phormat_hasColorFunction) {
            info.setReturnValue(Integer.toHexString(this.phormat_previousColor));
        }
    }
}
