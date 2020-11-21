package user11681.thorium.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import user11681.thorium.Thorium;
import user11681.thorium.client.render.block.entity.SoulSandBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class ThoriumClient implements ClientModInitializer {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(Thorium.SOUL_SAND, SoulSandBlockEntityRenderer::new);
    }
}
