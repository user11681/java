package user11681.sturdygold.asm.mixin;

import net.minecraft.item.ToolMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@SuppressWarnings("UnresolvedMixinReference")
@Mixin(ToolMaterials.class)
abstract class ToolMaterialsMixin {
    @ModifyArgs(method = "<clinit>",
                at = @At(value = "INVOKE",
                         target = "net/minecraft/item/ToolMaterials.<init>(Ljava/lang/String;IIIFFILjava/util/function/Supplier;)V"))
    private static void fixGold(final Args arguments) {
        if (arguments.get(0).equals("GOLD")) {
            arguments.set(2, 2);
            arguments.set(3, 768);
            arguments.set(5, 2F);
        }
    }
}
