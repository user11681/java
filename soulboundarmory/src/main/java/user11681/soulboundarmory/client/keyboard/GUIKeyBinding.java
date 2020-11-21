package user11681.soulboundarmory.client.keyboard;

import org.lwjgl.glfw.GLFW;
import user11681.soulboundarmory.SoulboundArmory;
import user11681.soulboundarmory.SoulboundArmoryClient;
import user11681.soulboundarmory.component.soulbound.item.ItemStorage;
import user11681.soulboundarmory.component.soulbound.item.StorageType;
import user11681.usersmanual.client.keyboard.ModKeyBinding;

public class GUIKeyBinding extends ModKeyBinding {
    public GUIKeyBinding() {
        super(SoulboundArmory.key("menu"), GLFW.GLFW_KEY_R, SoulboundArmory.category(SoulboundArmory.NAME));
    }

    @Override
    protected void onPress() {
        final ItemStorage<?> component = StorageType.getFirstMenuStorage(SoulboundArmoryClient.getPlayer());

        if (component != null) {
            component.openGUI();
        }
    }
}
