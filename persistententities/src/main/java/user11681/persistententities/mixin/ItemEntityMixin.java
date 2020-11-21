package user11681.persistententities.mixin;

import net.minecraft.entity.ItemEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import user11681.persistententities.config.PersistentEntitiesConfiguration;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @ModifyConstant(method = "tick",
                    constant = @Constant(intValue = 6000))
    public int modifyDespawnTime(final int previous) {
        return PersistentEntitiesConfiguration.Data.itemTime;
    }

    @Redirect(method = "tick",
              slice = @Slice(from = @At(value = "INVOKE_ASSIGN",
                                        target = "Lnet/minecraft/util/math/Vec3d;lengthSquared()D")),
              at = @At(value = "FIELD",
                       opcode = Opcodes.GETFIELD,
                       target = "Lnet/minecraft/entity/ItemEntity;age:I"))
    public int disableDespawn(final ItemEntity entity) {
        return PersistentEntitiesConfiguration.Data.disableItems ? -1 : entity.getAge();
    }
}
