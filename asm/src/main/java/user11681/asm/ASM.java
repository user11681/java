package user11681.asm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ASM implements ModInitializer, ClientModInitializer {
    public static final String ID = "asm";

    public static Identifier id(final String path) {
        return new Identifier(ID, path);
    }

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, id("asm_block"), ASMBlocks.ASM);
        Registry.register(Registry.ITEM, id("asm_block"), ASMItems.ASM);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ASMBlocks.ASM, RenderLayer.getTranslucent());
    }
}
