package user11681.persistententities.mixin;

import net.minecraft.entity.mob.MobEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import user11681.persistententities.config.PersistentEntitiesConfiguration;

@Mixin(MobEntity.class)
abstract class MobEntityMixin {
    @ModifyConstant(method =  "checkDespawn",
                    constant = @Constant(intValue = 600))
    public int modifyDespawnTime(final int previous) {
        return PersistentEntitiesConfiguration.Data.mobTime;
    }

    @Redirect(method = "checkDespawn",
              at = @At(value = "FIELD",
                       opcode = Opcodes.GETFIELD,
                       target = "Lnet/minecraft/entity/mob/MobEntity;despawnCounter:I",
                       ordinal = 0))
    public int disableDespawn(final MobEntity entity) {
        return PersistentEntitiesConfiguration.Data.disableMobs ? -1 : entity.getDespawnCounter();
    }
}
