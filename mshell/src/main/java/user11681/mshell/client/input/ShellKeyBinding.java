package user11681.mshell.client.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.options.KeyBinding;
import org.lwjgl.glfw.GLFW;
import user11681.mshell.ModID;

@Environment(EnvType.CLIENT)
public class ShellKeyBinding extends KeyBinding {
    public static final ShellKeyBinding KEY = new ShellKeyBinding();

    public ShellKeyBinding() {
        super(ModID.key("open"), GLFW.GLFW_KEY_M, ModID.NAME);
    }
}
