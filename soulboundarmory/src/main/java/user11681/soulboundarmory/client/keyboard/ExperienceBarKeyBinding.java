package user11681.soulboundarmory.client.keyboard;

import org.lwjgl.glfw.GLFW;
import user11681.soulboundarmory.SoulboundArmory;
import user11681.soulboundarmory.config.Configuration;
import user11681.usersmanual.client.keyboard.ModKeyBinding;

public class ExperienceBarKeyBinding extends ModKeyBinding {
    public ExperienceBarKeyBinding() {
        super(SoulboundArmory.key("bar"), GLFW.GLFW_KEY_X, SoulboundArmory.category(SoulboundArmory.NAME));
    }

    @Override
    protected void onPress() {
        final Configuration.Client client = Configuration.instance().client;

        client.toggleOverlayExperienceBar();
    }
}
