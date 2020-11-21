package user11681.visp.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config.Gui.Background;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Excluded;
import user11681.visp.Visp;

@Config(name = Visp.ID)
@Background("textures/block/andesite.png")
public class VispConfiguration implements ConfigData {
    @Excluded
    public static transient VispConfiguration instance;

    public boolean highlightExistingItemStacks = false;
}
