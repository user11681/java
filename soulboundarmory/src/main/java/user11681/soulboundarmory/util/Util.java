package user11681.soulboundarmory.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;

public class Util {
    @SuppressWarnings("deprecation")
    public static MinecraftServer getServer() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT
            ? MinecraftClient.getInstance().getServer()
            : (MinecraftServer) FabricLoader.getInstance().getGameInstance();
    }
}
