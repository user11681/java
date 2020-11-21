package user11681.thorium.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config.Gui.Background;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Category;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Tooltip;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.TransitiveObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import user11681.thorium.Thorium;
import user11681.thorium.client.ThoriumClient;

@Config(name = Thorium.MOD_ID)
@Background("minecraft:textures/block/andesite.png")
public class ThoriumConfiguration implements ConfigData {
    @TransitiveObject
    @Category("client")
    @Environment(EnvType.CLIENT)
    final Client client = new Client();

    @Tooltip(count = 3)
    boolean brownSand = true;

    public ThoriumConfiguration() {
        ThoriumConfiguration.Data.set(this);
    }

    public static class Client {
        @Environment(EnvType.CLIENT)
        @Tooltip
        boolean hideRecipeBook = true;

        @Environment(EnvType.CLIENT)
        @Tooltip
        boolean disableMusic = true;
    }

    public static class Data {
        public static boolean brownSand;

        @Environment(EnvType.CLIENT)
        public static class Client {
            public static boolean hideRecipeBook;
            public static boolean disableMusic;
        }

        public static void set(final ThoriumConfiguration configuration) {
            Client.hideRecipeBook = configuration.client.hideRecipeBook;
            Client.disableMusic = configuration.client.disableMusic;

            if (configuration.brownSand ^ ThoriumConfiguration.Data.brownSand) {
                brownSand = configuration.brownSand;

                ColorProviderRegistry.BLOCK.register((final BlockState state, final BlockRenderView view, final BlockPos pos, final int index) -> Data.getColor(), Blocks.SOUL_SAND);
                ColorProviderRegistry.ITEM.register((final ItemStack itemStack, final int tintIndex) -> Data.getColor(), Blocks.SOUL_SAND);

                if (ThoriumClient.CLIENT.world != null) {
                    ThoriumClient.CLIENT.worldRenderer.reload();
                }
            }
        }

        private static int getColor() {
            return Data.brownSand ? 0x5B4538 : 0xFFFFFF;
        }
    }
}
