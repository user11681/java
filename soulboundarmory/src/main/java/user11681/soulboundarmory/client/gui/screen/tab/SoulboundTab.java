package user11681.soulboundarmory.client.gui.screen.tab;

import com.mojang.blaze3d.systems.RenderSystem;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import user11681.soulboundarmory.SoulboundArmoryClient;
import user11681.soulboundarmory.client.gui.ExperienceBarOverlay;
import user11681.soulboundarmory.client.gui.ExperienceBarOverlay.Style;
import user11681.soulboundarmory.client.gui.RGBASlider;
import user11681.soulboundarmory.client.i18n.Translations;
import user11681.soulboundarmory.component.soulbound.item.ItemStorage;
import user11681.soulboundarmory.component.soulbound.player.SoulboundComponent;
import user11681.soulboundarmory.component.statistics.Category;
import user11681.soulboundarmory.config.Configuration;
import user11681.soulboundarmory.network.common.ExtendedPacketBuffer;
import user11681.soulboundarmory.registry.Packets;
import user11681.spun.client.gui.screen.ScreenTab;
import user11681.spun.client.gui.widget.SpunWidget;
import user11681.spun.client.gui.widget.SpunWidget.PressAction;

import static user11681.soulboundarmory.component.statistics.StatisticType.EXPERIENCE;
import static user11681.soulboundarmory.component.statistics.StatisticType.LEVEL;

@Environment(EnvType.CLIENT)
public abstract class SoulboundTab extends ScreenTab {
    protected static final NumberFormat FORMAT = DecimalFormat.getInstance();

    protected final PlayerEntity player;
    protected final List<AbstractButtonWidget> options;
    protected final List<RGBASlider> sliders;
    protected final SoulboundComponent component;

    protected ItemStorage<?> storage;
    protected ItemStack itemStack;
    protected ExperienceBarOverlay xpBar;
    protected int slot;

    public SoulboundTab(final Text title, final SoulboundComponent component, final List<ScreenTab> tabs) {
        super(title, tabs);

        this.player = SoulboundArmoryClient.getPlayer();
        this.component = component;
        this.options = new ArrayList<>(5);
        this.sliders = new ArrayList<>(4);
    }

    @Override
    public void init() {
        this.storage = this.component.getMenuStorage();
        this.itemStack = this.component.getMenuStorage().getItemStack();

        for (final ItemStack itemStack : this.player.getItemsHand()) {
            if (itemStack.equals(this.itemStack)) {
                this.itemStack = itemStack;

                break;
            }
        }

        super.init();

        if (this.displayTabs()) {
            this.storage.setCurrentTab(this.index);

            final Text text = this.slot != storage.getBoundSlot() ? Translations.MENU_BUTTON_BIND : Translations.MENU_BUTTON_UNBIND;
            final int buttonWidth = Math.max(this.tab.getWidth(), this.textRenderer.getWidth(text) + 8);

            this.addButton(new ButtonWidget(this.tab.getEndX() - buttonWidth, this.height - this.height / 16 - 20, buttonWidth, 20, text, this.bindSlotAction()));

            if (this.displayXPBar()) {
                this.xpBar = new ExperienceBarOverlay(this.storage);

                this.initSettings();
            }
        }
    }

    protected boolean initSettings() {
        if (Configuration.instance().client.displayOptions) {
            if (this.options.isEmpty()) {
                final Configuration.Client configuration = Configuration.instance().client;
                final Configuration.Client.Colors colors = configuration.colors;

                this.sliders.add(this.addButton(this.colorSlider(colors.red, Translations.RED, 0)));
                this.sliders.add(this.addButton(this.colorSlider(colors.green, Translations.GREEN, 1)));
                this.sliders.add(this.addButton(this.colorSlider(colors.blue, Translations.BLUE, 2)));
                this.sliders.add(this.addButton(this.colorSlider(colors.alpha, Translations.ALPHA, 3)));
                this.options.add(this.addButton(this.optionButton(4, new TranslatableText("%s: %s", Translations.XP_BAR_STYLE, configuration.style), this.cycleStyleAction(1), this.cycleStyleAction(-1))));
                this.options.addAll(sliders);

                return true;
            }

            this.addButtons(this.options);
        }

        return false;
    }

    @Override
    protected boolean displayTabs() {
        return this.slot > -1 && this.storage.isUnlocked();
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float partialTicks) {
        this.withZ(-500, () -> this.renderBackground(matrices));

        super.render(matrices, mouseX, mouseY, partialTicks);

        if (this.displayXPBar()) {
            this.drawXPBar(matrices, mouseX, mouseY);
        }
    }

    protected void drawXPBar(final MatrixStack matrices, final int mouseX, final int mouseY) {
        final int xp = this.storage.getDatum(EXPERIENCE);
        final int maxLevel = Configuration.instance().maxLevel;

        this.xpBar.render();

        if (this.isMouseOverLevel(mouseX, mouseY) && maxLevel >= 0) {
            this.renderTooltip(matrices, new TranslatableText("%s/%s", this.storage.getDatum(LEVEL), maxLevel), mouseX, mouseY);
        } else if (this.isMouseOverXPBar(mouseX, mouseY)) {
            this.renderTooltip(matrices, this.storage.canLevelUp()
                    ? new TranslatableText("%s/%s", xp, this.storage.getNextLevelXP())
                    : new TranslatableText("%s", xp), mouseX, mouseY);
        }

        RenderSystem.disableLighting();
    }

    protected boolean displayXPBar() {
        return this.displayTabs();
    }

    protected boolean isMouseOverXPBar(final double mouseX, final double mouseY) {
        final double barX = this.getXPBarX();
        final double barY = this.getXPBarY();

        return this.displayXPBar() && mouseX >= barX && mouseX <= barX + 182 && mouseY >= barY && mouseY <= barY + 4;
    }

    protected int getXPBarY() {
        return this.height - 29;
    }

    protected int getXPBarX() {
        return (this.width - 182) / 2;
    }

    protected boolean isMouseOverLevel(final int mouseX, final int mouseY) {
        final String levelString = "" + this.storage.getDatum(LEVEL);

        final int levelLeftX = (this.width - this.textRenderer.getWidth(levelString)) / 2;
        final int levelTopY = height - 35;

        return mouseX >= levelLeftX && mouseX <= levelLeftX + this.textRenderer.getWidth(levelString)
                && mouseY >= levelTopY && mouseY <= levelTopY + this.textRenderer.fontHeight;
    }

    protected RGBASlider sliderMousedOver(final double mouseX, final double mouseY) {
        for (int slider = 0; slider < 4; slider++) {
            if (mouseX >= this.getOptionX() && mouseX <= this.getOptionX() + 100
                    && mouseY >= this.getOptionY(slider) && mouseY <= this.getOptionY(slider) + 20) {
                return this.sliders.get(slider);
            }
        }

        return null;
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (this.isMouseOverXPBar(mouseX, mouseY)) {
            if (Configuration.instance().client.displayOptions) {
                this.removeButtons(this.options);
                Configuration.instance().client.displayOptions = false;
            } else {
                this.addButtons(this.options);
                Configuration.instance().client.displayOptions = true;
            }

            this.refresh();

            return true;
        }

        return false;
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (SoulboundArmoryClient.GUI_KEY_BINDING.matchesKey(keyCode, scanCode)) {
            this.onClose();
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(final double x, final double y, final double dWheel) {
        final RGBASlider slider = this.sliderMousedOver(x, y);

        if (slider != null) {
            slider.scroll(dWheel);

            return true;
        }

        return super.mouseScrolled(x, y, dWheel);
    }

    protected void cycleStyle(final int change) {
        final int index = (Style.STYLES.indexOf(Configuration.instance().client.style) + change) % Style.AMOUNT;

        if (index < 0) {
            this.cycleStyle(Style.AMOUNT + index);
        } else {
            Configuration.instance().client.style = Style.STYLES.get(index);
            this.refresh();
        }
    }

    public ButtonWidget centeredButton(final int y, final int buttonWidth, final Text text, final PressAction action) {
        return new ButtonWidget((this.width - buttonWidth) / 2, y, buttonWidth, 20, text, action);
    }

    public ButtonWidget squareButton(final int x, final int y, final Text text, final PressAction action) {
        return new ButtonWidget(x - 10, y - 10, 20, 20, text, action);
    }

    public ButtonWidget resetButton(final PressAction action) {
        return new ButtonWidget(this.width - this.width / 24 - 112, this.height - this.height / 16 - 20, 112, 20, Translations.MENU_BUTTON_RESET, action);
    }

    public SpunWidget optionButton(final int row, final Text text, final PressAction primaryAction, final PressAction secondaryAction) {
        final SpunWidget button = new SpunWidget(this.getOptionX(), this.getOptionY(row), 100, 20, text);

        button.setPrimaryAction(primaryAction);
        button.secondaryAction = secondaryAction;

        return button;
    }

    public RGBASlider colorSlider(final double currentValue, final Text text, final int id) {
        return new RGBASlider(this.getOptionX(), this.getOptionY(id), 100, 20, text, currentValue, id);
    }

    public ButtonWidget[] addPointButtons(final int rows, final int points, final PressAction action) {
        final ButtonWidget[] buttons = new ButtonWidget[rows];
        final int start = (this.height - (rows - 1) * this.height / 16) / 2;

        for (int row = 0; row < rows; row++) {
            buttons[row] = squareButton((this.width + 162) / 2, start + row * this.height / 16 + 4, new LiteralText("+"), action);
            buttons[row].active = points > 0;
        }

        return buttons;
    }

    public ButtonWidget[] removePointButtons(final int rows, final PressAction action) {
        final ButtonWidget[] buttons = new ButtonWidget[rows];
        final int start = (this.height - (rows - 1) * this.height / 16) / 2;

        for (int row = 0; row < rows; row++) {
            buttons[row] = this.squareButton((this.width + 162) / 2 - 20, start + row * this.height / 16 + 4, new LiteralText("-"), action);
        }

        return buttons;
    }

    public int getOptionX() {
        return Math.round(this.width * (1 - 1 / 24F)) - 100;
    }

    public int getOptionY(final int row) {
        return this.height / 16 + Math.max(this.height / 16 * row, 30 * row);
    }

    protected PressAction bindSlotAction() {
        return (final SpunWidget button) ->
                SoulboundArmoryClient.PACKET_REGISTRY.sendToServer(Packets.C2S_BIND_SLOT, new ExtendedPacketBuffer(this.storage).writeInt(this.slot));
    }

    protected PressAction cycleStyleAction(final int change) {
        return (final SpunWidget button) -> this.cycleStyle(change);
    }

    protected PressAction resetAction(final Category category) {
        return (final SpunWidget button) ->
                SoulboundArmoryClient.PACKET_REGISTRY.sendToServer(Packets.C2S_RESET, new ExtendedPacketBuffer(this.storage).writeIdentifier(category.getIdentifier()));
    }
}
