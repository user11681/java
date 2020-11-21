package user11681.thorium.client.integration;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import user11681.thorium.config.ThoriumConfiguration;

@Environment(EnvType.CLIENT)
public class ThoriumModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (final Screen parent) -> AutoConfig.getConfigScreen(ThoriumConfiguration.class, parent).get();
    }
}
