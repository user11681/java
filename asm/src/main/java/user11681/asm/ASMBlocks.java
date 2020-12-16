package user11681.asm;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class ASMBlocks {
    private static final AbstractBlock.ContextPredicate never = ASMBlocks::never;

    public static final ASMBlock ASM = new ASMBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.LAPIS).strength(3).nonOpaque().requiresTool().blockVision(never).solidBlock(never).suffocates(never));

    private static boolean never(final BlockState state, final BlockView view, final BlockPos pos) {
        return false;
    }
}
