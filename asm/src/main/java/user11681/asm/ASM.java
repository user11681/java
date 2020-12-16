package user11681.asm;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.api.ModInitializer;

public class ASM implements ModInitializer {
    public static final String ID = "asm";

    public static Identifier id(final String path) {
        return new Identifier(ID, path);
    }

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, id("asm_block"), ASMBlocks.ASM);
        Registry.register(Registry.ITEM, id("asm_block"), ASMItems.ASM);

        ASMItems.ASM.appendBlocks(Item.BLOCK_ITEMS, ASMItems.ASM);
    }
}
