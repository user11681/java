package user11681.mshell.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import user11681.mshell.client.input.ShellKeyBinding;
import user11681.mshell.client.screen.MShellScreen;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class MShellClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(ShellKeyBinding.KEY);

        ClientTickEvents.END_CLIENT_TICK.register((final MinecraftClient client) -> {
            if (ShellKeyBinding.KEY.isPressed()) {
                client.openScreen(new MShellScreen());
            }
        });
    }
}
