package user11681.thorium.asm.mixin.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
    @Shadow
    @Deprecated
    public native BlockState getStateForNeighborUpdate(final BlockState state, final Direction direction, final BlockState newState, final WorldAccess world, final BlockPos pos, final BlockPos posFrom);
}
