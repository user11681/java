package user11681.headsdowndisplay;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.options.KeyBinding;
import user11681.headsdowndisplay.asm.HDDMixinConfigPlugin;
import user11681.headsdowndisplay.config.HDDConfig;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class HDD implements ModInitializer  {
    public static final KeyBinding KEY = new KeyBinding("key.headsdowndisplay.toggle", -1, "key.category.headsdowndisplay");

    public void onInitialize() {
        HDDConfig.instance = AutoConfig.register(HDDConfig.class, Toml4jConfigSerializer::new).get();

        KeyBindingRegistryImpl.registerKeyBinding(KEY);
    }

    public static void registerQuadAlphaRedirector(final Float2FloatFunction newAlpha) {
        if (HDDMixinConfigPlugin.quadAlphaRedirector == null) {
            HDDMixinConfigPlugin.quadAlphaRedirector = newAlpha;
            HDDMixinConfigPlugin.applyRedirector = true;
        } else {
            HDDMixinConfigPlugin.quadAlphaRedirector = (final float alpha) -> newAlpha.get(HDDMixinConfigPlugin.quadAlphaRedirector.get(alpha));
        }
    }
}
