package user11681.soulboundarmory.client.gui.screen.tab;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import user11681.soulboundarmory.SoulboundArmoryClient;
import user11681.soulboundarmory.client.i18n.Translations;
import user11681.soulboundarmory.component.soulbound.player.SoulboundComponent;
import user11681.soulboundarmory.component.statistics.Statistic;
import user11681.soulboundarmory.network.common.ExtendedPacketBuffer;
import user11681.soulboundarmory.registry.Packets;
import user11681.usersmanual.client.gui.screen.ScreenTab;
import user11681.usersmanual.collections.ArrayMap;

import static user11681.soulboundarmory.component.statistics.Category.ATTRIBUTE;
import static user11681.soulboundarmory.component.statistics.StatisticType.ATTRIBUTE_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.SPENT_ATTRIBUTE_POINTS;

public class AttributeTab extends SoulboundTab {
    protected ArrayMap<Statistic, Text> attributes;

    public AttributeTab(final SoulboundComponent component, final List<ScreenTab> tabs) {
        super(Translations.MENU_BUTTON_ATTRIBUTES, component, tabs);
    }

    @Override
    public void init(final MinecraftClient client, final int width, final int height) {
        super.init(client, width, height);

        final ArrayMap<Statistic, Text> attributes = this.attributes = this.storage.getScreenAttributes();
        final int size = attributes.size();
        final int start = (this.height - (size - 1) * this.height / 16) / 2;
        final ButtonWidget resetButton = this.addButton(this.resetButton(this.resetAction(ATTRIBUTE)));

        resetButton.active = this.storage.getDatum(SPENT_ATTRIBUTE_POINTS) > 0;

        for (int index = 0; index < size; index++) {
            final Statistic attribute = attributes.getKey(index);
            final ButtonWidget add = this.addButton(this.squareButton((this.width + 182) / 2, start + index * this.height / 16 + 4, new LiteralText("+"), this.addPointAction(attribute)));
            final ButtonWidget remove = this.addButton(this.squareButton((this.width + 182) / 2 - 20, start + index * this.height / 16 + 4, new LiteralText("-"), this.removePointAction(attribute)));

            remove.active = attribute.isAboveMin();
            add.active = this.storage.getDatum(ATTRIBUTE_POINTS) > 0 && attribute.isBelowMax();
        }
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float tickDelta) {
        super.render(matrices, mouseX, mouseY, tickDelta);

        final int points = this.storage.getDatum(ATTRIBUTE_POINTS);

        if (points > 0) {
            this.drawCenteredString(matrices, this.textRenderer, String.format("%s: %s", Translations.MENU_UNSPENT_POINTS, points), Math.round(width / 2F), 4, 0xFFFFFF);
        }

        final List<Text> values = this.attributes.values();

        for (int row = 0, size = values.size(); row < size; row++) {
            this.drawAttribute(matrices, values.get(row), row, size);
        }
    }

    public void drawAttribute(final MatrixStack stack, final Text format, final int row, final int rows) {
        TEXT_RENDERER.draw(stack, format, (this.width - 182) / 2F, this.getHeight(rows, row), 0xFFFFFF);
    }

    protected PressAction addPointAction(final Statistic statistic) {
        return (final ButtonWidget button) ->
                SoulboundArmoryClient.PACKET_REGISTRY.sendToServer(Packets.C2S_ATTRIBUTE, new ExtendedPacketBuffer(this.storage).writeIdentifier(statistic.getType().getIdentifier()).writeInt(hasShiftDown() ? this.storage.getDatum(ATTRIBUTE_POINTS) : 1));
    }

    protected PressAction removePointAction(final Statistic statistic) {
        return (final ButtonWidget button) ->
                SoulboundArmoryClient.PACKET_REGISTRY.sendToServer(Packets.C2S_ATTRIBUTE, new ExtendedPacketBuffer(this.storage).writeIdentifier(statistic.getType().getIdentifier()).writeInt(-(hasShiftDown() ? statistic.getPoints() : 1)));
    }

}
