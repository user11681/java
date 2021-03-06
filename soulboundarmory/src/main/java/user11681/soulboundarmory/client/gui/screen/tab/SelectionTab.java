package user11681.soulboundarmory.client.gui.screen.tab;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import user11681.soulboundarmory.SoulboundArmoryClient;
import user11681.soulboundarmory.component.soulbound.item.ItemStorage;
import user11681.soulboundarmory.component.soulbound.player.SoulboundComponent;
import user11681.soulboundarmory.network.common.ExtendedPacketBuffer;
import user11681.soulboundarmory.registry.Packets;
import user11681.usersmanual.client.gui.screen.ScreenTab;

public class SelectionTab extends SoulboundTab {
    public SelectionTab(final Text title, final SoulboundComponent component, final List<ScreenTab> tabs) {
        super(title, component, tabs);
    }

    @Override
    public void init(final MinecraftClient client, final int width, final int height) {
        super.init(client, width, height);

        final int buttonWidth = 128;
        final int buttonHeight = 20;
        final int centerX = (this.width - buttonWidth) / 2;
        final int ySep = 32;

        final List<ItemStorage<?>> selection = this.component.getStorages().values();

        selection.removeIf((final ItemStorage<?> storage) -> !storage.isUnlocked() && !storage.canUnlock());

        final int top = (this.height - buttonHeight - ySep * (selection.size() - 1)) / 2;

        for (int row = 0, size = selection.size(); row < size; row++) {
            final ItemStorage<?> storage = selection.get(row);
            final ButtonWidget button = this.addButton(new ButtonWidget(centerX, top + (row * ySep), buttonWidth, buttonHeight, storage.getName(), this.selectAction(storage)));

            if (this.displayTabs()) {
                button.active = storage.getType().getIdentifier() != this.storage.getType().getIdentifier();
            }
        }
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float partialTicks) {
        super.render(matrices, mouseX, mouseY, partialTicks);

        if (!this.storage.isItemEquipped()) {
            this.drawCenteredString(matrices, this.textRenderer, this.getLabel().toString(), this.width / 2, 40, 0xFFFFFF);
        }
    }

    protected PressAction selectAction(final ItemStorage<?> storage) {
        return (final ButtonWidget button) -> {
            SoulboundArmoryClient.PACKET_REGISTRY.sendToServer(Packets.C2S_ITEM_TYPE, new ExtendedPacketBuffer(storage).writeInt(this.slot));
            this.onClose();
        };
    }
}
