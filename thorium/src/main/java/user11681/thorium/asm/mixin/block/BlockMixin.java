package user11681.thorium.asm.mixin.block;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlockMixin {
    protected final Block self = (Block) (Object) this;

    @Shadow
    public native float getVelocityMultiplier();
}
