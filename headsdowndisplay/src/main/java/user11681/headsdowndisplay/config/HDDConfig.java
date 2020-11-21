package user11681.headsdowndisplay.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config.Gui.Background;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Category;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Excluded;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.TransitiveObject;
import user11681.headsdowndisplay.config.arm.ArmConfiguration;
import user11681.headsdowndisplay.config.hotbar.Hotbar;

@Config(name = "headsdowndisplay")
@Background("textures/block/andesite.png")
public class HDDConfig implements ConfigData {
    @Excluded
    public static transient HDDConfig instance;

    @Category("default")
    @TransitiveObject
    public final Hotbar hotbar = new Hotbar();

    @Category("arm")
    @TransitiveObject
    public final ArmConfiguration arm = new ArmConfiguration();

    @Override
    public void validatePostLoad() {
        this.hotbar.fadeEnd = this.hotbar.fadeDelay + this.hotbar.fadeDuration;
    }
}
