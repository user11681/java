package user11681.asm;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

@SuppressWarnings("deprecation")
public class ASMBlock extends AbstractGlassBlock {
    public static final VoxelShape shape = VoxelShapes.union(
        VoxelShapes.cuboid(0, 0, 0, 0.2, 1, 0.2),
        VoxelShapes.cuboid(0, 0, 0.2, 0.2, 0.2, 1),
        VoxelShapes.cuboid(0, 0.2, 0.8, 0.2, 0.4, 1),
        VoxelShapes.cuboid(0, 0.4, 0.2, 0.2, 0.6, 1),
        VoxelShapes.cuboid(0, 0.8, 0.2, 0.2, 1, 1),
        VoxelShapes.cuboid(0.2, 0.8, 0.8, 0.4, 1, 1),
        VoxelShapes.cuboid(0.4, 0, 0, 0.6, 1, 0.2),
        VoxelShapes.cuboid(0.4, 0, 0.2, 0.6, 0.2, 1),
        VoxelShapes.cuboid(0.4, 0.2, 0.8, 0.6, 0.4, 1),
        VoxelShapes.cuboid(0.4, 0.4, 0.2, 0.6, 0.6, 1),
        VoxelShapes.cuboid(0.4, 0.8, 0.2, 0.6, 1, 1),
        VoxelShapes.cuboid(0.6, 0.8, 0, 0.8, 1, 0.2),
        VoxelShapes.cuboid(0.8, 0, 0, 1, 1, 0.2),
        VoxelShapes.cuboid(0.8, 0, 0.2, 1, 0.2, 1),
        VoxelShapes.cuboid(0.8, 0.2, 0.8, 1, 0.4, 1),
        VoxelShapes.cuboid(0.8, 0.4, 0.2, 1, 0.6, 1),
        VoxelShapes.cuboid(0.8, 0.8, 0.2, 1, 1, 1)
    );

    public ASMBlock(final Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(final BlockState state, final BlockView world, final BlockPos pos, final ShapeContext context) {
        return shape;
    }
}
