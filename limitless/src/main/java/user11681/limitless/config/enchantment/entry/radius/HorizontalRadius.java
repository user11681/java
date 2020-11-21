package user11681.limitless.config.enchantment.entry.radius;

import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.BoundedDiscrete;

public class HorizontalRadius {
    @BoundedDiscrete(max = 64)
    public int min;

    @BoundedDiscrete(max = 64)
    public int max = 8;
}
