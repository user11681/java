package user11681.phormat.asm.mixin;

import it.unimi.dsi.fastutil.objects.Object2ReferenceArrayMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import user11681.phormat.api.ColorFunction;
import user11681.phormat.api.TextFormatter;
import user11681.phormat.asm.access.FormattingAccess;
import user11681.phormat.impl.FormattingInfo;
import user11681.phormat.impl.PhormatInitializer;

@SuppressWarnings("ConstantConditions")
@Mixin(Formatting.class)
abstract class FormattingMixin implements FormattingAccess {
    @Final
    @Shadow
    @Mutable
    private static Pattern FORMATTING_CODE_PATTERN;

    @Unique
    private ColorFunction colorFunction;

    @Unique
    private TextFormatter formatter;

    @Unique
    private boolean custom;

    @Override
    @Unique
    public boolean isCustom() {
        return this.custom;
    }

    @Override
    @Unique
    public Formatting cast() {
        return (Formatting) (Object) this;
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "<clinit>",
              at = @At(value = "INVOKE",
                       target = "Ljava/util/stream/Stream;collect(Ljava/util/stream/Collector;)Ljava/lang/Object;"))
    private static Object fixDuplicates(final Stream<Formatting> stream, Collector<Formatting, Object, Map<String, Formatting>> collector) {
        final Formatting[] formattings = stream.toArray(Formatting[]::new);
        final Map<String, Formatting> byName = new Object2ReferenceArrayMap<>(formattings.length);

        for (final Formatting formatting : formattings) {
            byName.put(formatting.getName(), formatting);
        }

        return byName;
    }

    @Inject(method = "getColorValue", at = @At("HEAD"), cancellable = true)
    public void applyColorFunction(final CallbackInfoReturnable<Integer> info) {
        if (this.custom) {
            final ColorFunction function = this.colorFunction;

            if (function != null) {
                info.setReturnValue(function.apply(info.getReturnValueI()));
            }
        }
    }

    @Override
    public ColorFunction getColorFunction() {
        return this.colorFunction;
    }

    @Override
    public void setColorFunction(final ColorFunction colorFunction) {
        this.colorFunction = colorFunction;
    }

    @Override
    public TextFormatter getFormatter() {
        return this.formatter;
    }

    @Override
    public void setFormatter(final TextFormatter formatter) {
        this.formatter = formatter;
    }

    static {
        final String pattern = FORMATTING_CODE_PATTERN.toString();

        for (final FormattingInfo info : PhormatInitializer.formattingInfo) {
            final FormattingMixin formatting = (FormattingMixin) (Object) Formatting.valueOf(info.name);

            formatting.colorFunction = info.colorFunction;
            formatting.formatter = info.formatter;
            formatting.custom = true;

            if (pattern.indexOf(info.code) >= 0) {
                throw new IllegalArgumentException(String.format("a formatting with the code %s already exists.", info.code));
            }

            FORMATTING_CODE_PATTERN = Pattern.compile(pattern.replace("]", info.code + "]"));
        }

        PhormatInitializer.formattingInfo = null;
    }
}
