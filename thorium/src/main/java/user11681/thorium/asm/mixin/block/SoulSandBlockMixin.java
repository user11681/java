package user11681.thorium.asm.mixin.block;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SoulSandBlock;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import user11681.thorium.config.ThoriumConfiguration;

@Mixin(SoulSandBlock.class)
public abstract class SoulSandBlockMixin extends BlockMixin {
    @Unique
    private static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 16);

    @Inject(method = "getCollisionShape", at = @At("RETURN"), cancellable = true)
    public void getCollisionShape(final BlockState state, final BlockView world, final BlockPos pos, final ShapeContext context, final CallbackInfoReturnable<VoxelShape> info) {
        if (ThoriumConfiguration.Data.brownSand) {
            info.setReturnValue(COLLISION_SHAPE);
        }
    }

    public float getVelocityMultiplier() {
        return ThoriumConfiguration.Data.brownSand ? 1 : super.getVelocityMultiplier();
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(final BlockState state, final Direction facing, final BlockState neighborState, final WorldAccess world, final BlockPos pos, final BlockPos neighborPos) {
        if (ThoriumConfiguration.Data.brownSand) {
            world.getBlockTickScheduler().schedule(pos, self, 2);
        }

        return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
    }

    @Inject(method = "scheduledTick", at = @At("RETURN"))
    public void scheduledTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random, final CallbackInfo info) {
        if (ThoriumConfiguration.Data.brownSand && FallingBlock.canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= 0) {
            world.spawnEntity(new FallingBlockEntity(
                world,
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                world.getBlockState(pos)
            ));
        }
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(final BlockState state, final World world, final BlockPos pos, final Random random) {
        if (ThoriumConfiguration.Data.brownSand && random.nextInt(16) == 0
            && FallingBlock.canFallThrough(world.getBlockState(pos.down()))) {
            world.addParticle(new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, state),
                pos.getX() + random.nextFloat(),
                pos.getY() - 0.05,
                pos.getZ() + random.nextFloat(),
                0, 0, 0
            );
        }
    }
}
