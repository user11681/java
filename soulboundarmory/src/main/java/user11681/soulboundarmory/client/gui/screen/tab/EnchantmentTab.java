package user11681.soulboundarmory.client.gui.screen.tab;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;
import user11681.soulboundarmory.SoulboundArmoryClient;
import user11681.soulboundarmory.client.i18n.Translations;
import user11681.soulboundarmory.component.soulbound.item.ItemStorage;
import user11681.soulboundarmory.component.soulbound.player.SoulboundComponent;
import user11681.soulboundarmory.network.common.ExtendedPacketBuffer;
import user11681.soulboundarmory.registry.Packets;
import user11681.usersmanual.client.gui.screen.ScreenTab;
import user11681.usersmanual.collections.ArrayMap;

import static user11681.soulboundarmory.component.statistics.Category.ENCHANTMENT;
import static user11681.soulboundarmory.component.statistics.StatisticType.ENCHANTMENT_POINTS;
import static user11681.soulboundarmory.component.statistics.StatisticType.SPENT_ENCHANTMENT_POINTS;

public class EnchantmentTab extends SoulboundTab {
    public EnchantmentTab(final SoulboundComponent component, final List<ScreenTab> tabs) {
        super(Translations.MENU_BUTTON_ENCHANTMENTS, component, tabs);
    }

    @Override
    public void init(final MinecraftClient client, final int width, final int height) {
        super.init(client, width, height);

        final ItemStorage<?> storage = this.storage;
        final ArrayMap<Enchantment, Integer> enchantments = storage.getEnchantments();
        final int size = enchantments.size();
        final ButtonWidget resetButton = this.addButton(this.resetButton(this.resetAction(ENCHANTMENT)));
        resetButton.active = storage.getDatum(SPENT_ENCHANTMENT_POINTS) > 0;

        for (int row = 0; row < size; row++) {
            final ButtonWidget disenchant = this.addButton(this.squareButton((width + 162) / 2 - 20, this.getHeight(size, row), new LiteralText("-"), this.disenchantAction(enchantments.getKey(row))));
            final ButtonWidget enchant = this.addButton(this.squareButton((width + 162) / 2, this.getHeight(size, row), new LiteralText("+"), this.enchantAction(enchantments.getKey(row))));
            disenchant.active = enchantments.get(row) > 0;
            enchant.active = storage.getDatum(ENCHANTMENT_POINTS) > 0;
        }
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float partialTicks) {
        super.render(matrices, mouseX, mouseY, partialTicks);

        final ArrayMap<Enchantment, Integer> enchantments = this.storage.getEnchantments();
        final int points = this.storage.getDatum(ENCHANTMENT_POINTS);

        if (points > 0) {
            this.drawCenteredString(matrices, this.textRenderer, String.format("%s: %d", Translations.MENU_UNSPENT_POINTS, points), Math.round(width / 2F), 4, 0xFFFFFF);
        }

        for (int row = 0, size = enchantments.size(); row < size; row++) {
            TEXT_RENDERER.draw(matrices, enchantments.getKey(row).getName(enchantments.get(row)), (this.width - 182) / 2F, this.getHeight(size, row) - TEXT_RENDERER.fontHeight / 2F, 0xFFFFFF);
        }
    }

    protected PressAction enchantAction(final Enchantment enchantment) {
        return (final ButtonWidget button) ->
                SoulboundArmoryClient.PACKET_REGISTRY.sendToServer(Packets.C2S_ENCHANT, new ExtendedPacketBuffer(this.storage).writeIdentifier(Registry.ENCHANTMENT.getId(enchantment)).writeBoolean(true).writeBoolean(hasShiftDown()));
    }

    protected PressAction disenchantAction(final Enchantment enchantment) {
        return (final ButtonWidget button) ->
                SoulboundArmoryClient.PACKET_REGISTRY.sendToServer(Packets.C2S_ENCHANT, new ExtendedPacketBuffer(this.storage).writeIdentifier(Registry.ENCHANTMENT.getId(enchantment)).writeBoolean(false).writeBoolean(hasShiftDown()));
    }
}
