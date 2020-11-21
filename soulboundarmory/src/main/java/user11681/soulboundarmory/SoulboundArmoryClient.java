package user11681.soulboundarmory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import user11681.soulboundarmory.client.keyboard.ExperienceBarKeyBinding;
import user11681.soulboundarmory.client.keyboard.GUIKeyBinding;
import user11681.soulboundarmory.client.render.SoulboundDaggerEntityRenderer;
import user11681.soulboundarmory.client.render.SoulboundFireballEntityRenderer;
import user11681.soulboundarmory.entity.EntityTypes;
import user11681.usersmanual.client.keyboard.ModKeyBinding;

@Environment(EnvType.CLIENT)
public class SoulboundArmoryClient implements ClientModInitializer {
    public static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static final ModKeyBinding GUI_KEY_BINDING = new GUIKeyBinding();
    public static final ModKeyBinding TOGGLE_XP_BAR_KEY_BINDING = new ExperienceBarKeyBinding();

    public static final ClientSidePacketRegistry PACKET_REGISTRY = ClientSidePacketRegistry.INSTANCE;

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(GUI_KEY_BINDING);
        KeyBindingHelper.registerKeyBinding(TOGGLE_XP_BAR_KEY_BINDING);

        EntityRendererRegistry.INSTANCE.register(EntityTypes.SOULBOUND_DAGGER_ENTITY, SoulboundDaggerEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(EntityTypes.SOULBOUND_FIREBALL_ENTITY, SoulboundFireballEntityRenderer::new);
    }

    public static PlayerEntity getPlayer() {
        return CLIENT.player;
    }
}
