package user11681.cell.container;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_5632;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public abstract class Container extends ScreenHandler {
    public final PlayerEntity player;
    public final PlayerInventory playerInventory;

    @Environment(EnvType.CLIENT)
    DelegatingHandledScreen screen;

    @Environment(EnvType.CLIENT)
    String method;

    public Container(final int syncId, final PlayerInventory playerInventory) {
        this(null, syncId, playerInventory, playerInventory.player);
    }

    public Container(final ScreenHandlerType<? extends Container> type, final int syncId, final PlayerInventory playerInventory, final PlayerEntity player) {
        super(type, syncId);

        this.playerInventory = playerInventory;
        this.player = player;
    }

    public static <T extends Container> ScreenHandlerType<T> register(final Identifier identifier, final ScreenHandlerRegistry.SimpleClientHandlerFactory<T> containerSupplier) {
        final ScreenHandlerType<T> type = ScreenHandlerRegistry.registerSimple(identifier, containerSupplier);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ScreenRegistry.register(type, DelegatingHandledScreen::new);
        }

        return type;
    }

    public abstract boolean canUse(final PlayerEntity player);

    protected abstract void drawBackground(final MatrixStack matrices, final float delta, final int mouseX, final int mouseY);

    @Environment(EnvType.CLIENT)
    protected void init() {
        this.method = "init0";

        this.screen.init();
    }

    @Environment(EnvType.CLIENT)
    protected void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
        this.method = "render";

        this.screen.render(matrices, mouseX, mouseY, delta);
    }

    @Environment(EnvType.CLIENT)
    protected void drawMouseoverTooltip(final MatrixStack matrices, final int x, final int y) {
        this.method = "drawMouseoverTooltip";

        this.screen.drawMouseoverTooltip(matrices, x, y);
    }

    @Environment(EnvType.CLIENT)
    protected void drawForeground(final MatrixStack matrices, final int mouseX, final int mouseY) {
        this.method = "drawForeground";

        this.screen.drawForeground(matrices, mouseX, mouseY);
    }

    @Environment(EnvType.CLIENT)
    protected boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        this.method = "mouseClicked";

        return this.screen.mouseClicked(mouseX, mouseY, button);
    }

    @Environment(EnvType.CLIENT)
    protected boolean isClickOutsideBounds(final double mouseX, final double mouseY, final int left, final int top, final int button) {
        this.method = "isClickOutsideBounds";

        return this.screen.isClickOutsideBounds(mouseX, mouseY, left, top, button);
    }

    @Environment(EnvType.CLIENT)
    protected boolean mouseDragged(final double mouseX, final double mouseY, final int button, final double deltaX, final double deltaY) {
        this.method = "mouseDragged";

        return this.screen.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Environment(EnvType.CLIENT)
    protected boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        this.method = "mouseReleased";

        return this.screen.mouseReleased(mouseX, mouseY, button);
    }

    @Environment(EnvType.CLIENT)
    protected boolean isPointWithinBounds(final int xPosition, final int yPosition, final int width, final int height, final double pointX, final double pointY) {
        this.method = "isPointWithinBounds";

        return this.screen.isPointWithinBounds(xPosition, yPosition, width, height, pointX, pointY);
    }

    @Environment(EnvType.CLIENT)
    protected void onMouseClick(final Slot slot, final int invSlot, final int clickData, final SlotActionType actionType) {
        this.method = "onMouseClick";

        this.screen.onMouseClick(slot, invSlot, clickData, actionType);
    }

    @Environment(EnvType.CLIENT)
    protected boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        this.method = "keyPressed";

        return this.screen.keyPressed(keyCode, scanCode, modifiers);
    }

    @Environment(EnvType.CLIENT)
    protected boolean handleHotbarKeyPressed(final int keyCode, final int scanCode) {
        this.method = "handleHotbarKeyPressed";

        return this.screen.handleHotbarKeyPressed(keyCode, scanCode);
    }

    @Environment(EnvType.CLIENT)
    protected void removed() {
        this.method = "removed";

        this.screen.removed();
    }

    @Environment(EnvType.CLIENT)
    protected boolean isPauseScreen() {
        this.method = "isPauseScreen";

        return this.screen.isPauseScreen();
    }

    @Environment(EnvType.CLIENT)
    protected void tick() {
        this.method = "tick";

        this.screen.tick();
    }

    @Environment(EnvType.CLIENT)
    protected Container getScreenHandler() {
        this.method = "getScreenHandler";

        return this.screen.getScreenHandler();
    }

    @Environment(EnvType.CLIENT)
    protected void onClose() {
        this.method = "onClose";

        this.screen.onClose();
    }

    @Environment(EnvType.CLIENT)
    protected Text getTitle() {
        this.method = "getTitle";

        return this.screen.getTitle();
    }

    @Environment(EnvType.CLIENT)
    protected String getNarrationMessage() {
        this.method = "getNarrationMessage";

        return this.screen.getNarrationMessage();
    }

    @Environment(EnvType.CLIENT)
    protected boolean shouldCloseOnEsc() {
        this.method = "shouldCloseOnEsc";

        return this.screen.shouldCloseOnEsc();
    }

    @Environment(EnvType.CLIENT)
    protected <T extends AbstractButtonWidget> T addButton(final T button) {
        this.method = "addButton";

        return this.screen.addButton(button);
    }

    @Environment(EnvType.CLIENT)
    protected <T extends Element> T addChild(final T child) {
        this.method = "addChild";

        return this.screen.addChild(child);
    }

    @Environment(EnvType.CLIENT)
    protected void renderTooltip(final MatrixStack matrices, final ItemStack stack, final int x, final int y) {
        this.method = "renderTooltip0";

        this.screen.renderTooltip(matrices, stack, x, y);
    }

    @Environment(EnvType.CLIENT)
    protected void method_32634(final MatrixStack matrixStack, final List<Text> list, final Optional<class_5632> optional, final int i, final int j) {
        this.method = "method_32634";

        this.screen.method_32634(matrixStack, list, optional, i, j);
    }

    @Environment(EnvType.CLIENT)
    protected List<Text> getTooltipFromItem(final ItemStack stack) {
        this.method = "getTooltipFromItem";

        return this.screen.getTooltipFromItem(stack);
    }

    @Environment(EnvType.CLIENT)
    protected void renderTooltip(final MatrixStack matrices, final Text text, final int x, final int y) {
        this.method = "renderTooltip1";

        this.screen.renderTooltip(matrices, text, x, y);
    }

    @Environment(EnvType.CLIENT)
    protected void renderTooltip(final MatrixStack matrices, final List<Text> lines, final int x, final int y) {
        this.method = "renderTooltip2";

        this.screen.renderTooltip(matrices, lines, x, y);
    }

    @Environment(EnvType.CLIENT)
    protected void renderOrderedTooltip(final MatrixStack matrices, final List<? extends OrderedText> list, final int x, final int y) {
        this.method = "renderOrderedTooltip";

        this.screen.renderOrderedTooltip(matrices, list, x, y);
    }

    @Environment(EnvType.CLIENT)
    protected void renderTextHoverEffect(final MatrixStack matrices, @Nullable final Style style, final int x, final int y) {
        this.method = "renderTextHoverEffect";

        this.screen.renderTextHoverEffect(matrices, style, x, y);
    }

    @Environment(EnvType.CLIENT)
    protected void insertText(final String text, final boolean override) {
        this.method = "insertText";

        this.screen.insertText(text, override);
    }

    @Environment(EnvType.CLIENT)
    protected boolean handleTextClick(@Nullable final Style style) {
        this.method = "handleTextClick";

        return this.screen.handleTextClick(style);
    }

    @Environment(EnvType.CLIENT)
    protected void sendMessage(final String message) {
        this.method = "sendMessage0";

        this.screen.sendMessage(message);
    }

    @Environment(EnvType.CLIENT)
    protected void sendMessage(final String message, final boolean toHud) {
        this.method = "sendMessage1";

        this.screen.sendMessage(message, toHud);
    }

    @Environment(EnvType.CLIENT)
    protected void init(final MinecraftClient client, final int width, final int height) {
        this.method = "init1";

        this.screen.init(client, width, height);
    }

    @Environment(EnvType.CLIENT)
    protected List<? extends Element> children() {
        this.method = "children";

        return this.screen.children();
    }

    @Environment(EnvType.CLIENT)
    protected void renderBackground(final MatrixStack matrices) {
        this.method = "renderBackground0";

        this.screen.renderBackground(matrices);
    }

    @Environment(EnvType.CLIENT)
    protected void renderBackground(final MatrixStack matrices, final int vOffset) {
        this.method = "renderBackground1";

        this.screen.renderBackground(matrices, vOffset);
    }

    @Environment(EnvType.CLIENT)
    protected void renderBackgroundTexture(final int vOffset) {
        this.method = "renderBackgroundTexture";

        this.screen.renderBackgroundTexture(vOffset);
    }

    @Environment(EnvType.CLIENT)
    protected void resize(final MinecraftClient client, final int width, final int height) {
        this.method = "resize";

        this.screen.resize(client, width, height);
    }

    @Environment(EnvType.CLIENT)
    protected boolean isValidCharacterForName(final String name, final char character, final int cursorPos) {
        this.method = "isValidCharacterForName";

        return this.screen.isValidCharacterForName(name, character, cursorPos);
    }

    @Environment(EnvType.CLIENT)
    protected boolean isMouseOver(final double mouseX, final double mouseY) {
        this.method = "isMouseOver";

        return this.screen.isMouseOver(mouseX, mouseY);
    }

    @Environment(EnvType.CLIENT)
    protected void filesDragged(final List<Path> paths) {
        this.method = "filesDragged";

        this.screen.filesDragged(paths);
    }

    @Environment(EnvType.CLIENT)
    protected @Nullable Element getFocused() {
        this.method = "getFocused";

        return this.screen.getFocused();
    }

    @Environment(EnvType.CLIENT)
    protected void setFocused(@Nullable final Element focused) {
        this.method = "setFocused";

        this.screen.setFocused(focused);
    }

    @Environment(EnvType.CLIENT)
    protected void drawHorizontalLine(final MatrixStack matrices, final int x1, final int x2, final int y, final int color) {
        this.method = "drawHorizontalLine";

        this.screen.drawHorizontalLine(matrices, x1, x2, y, color);
    }

    @Environment(EnvType.CLIENT)
    protected void drawVerticalLine(final MatrixStack matrices, final int x, final int y1, final int y2, final int color) {
        this.method = "drawVerticalLine";

        this.screen.drawVerticalLine(matrices, x, y1, y2, color);
    }

    @Environment(EnvType.CLIENT)
    protected void fillGradient(final MatrixStack matrices, final int xStart, final int yStart, final int xEnd, final int yEnd, final int colorStart, final int colorEnd) {
        this.method = "fillGradient";

        this.screen.fillGradient(matrices, xStart, yStart, xEnd, yEnd, colorStart, colorEnd);
    }

    @Environment(EnvType.CLIENT)
    protected void method_29343(final int i, final int j, final BiConsumer<Integer, Integer> biConsumer) {
        this.method = "method_29343";

        this.screen.method_29343(i, j, biConsumer);
    }

    @Environment(EnvType.CLIENT)
    protected void drawTexture(final MatrixStack matrices, final int x, final int y, final int u, final int v, final int width, final int height) {
        this.method = "drawTexture";

        this.screen.drawTexture(matrices, x, y, u, v, width, height);
    }

    @Environment(EnvType.CLIENT)
    protected int getZOffset() {
        this.method = "getZOffset";

        return this.screen.getZOffset();
    }

    @Environment(EnvType.CLIENT)
    protected void setZOffset(final int zOffset) {
        this.method = "setZOffset";

        this.screen.setZOffset(zOffset);
    }

    @Environment(EnvType.CLIENT)
    protected Optional<Element> hoveredElement(final double mouseX, final double mouseY) {
        this.method = "hoveredElement";

        return this.screen.hoveredElement(mouseX, mouseY);
    }

    @Environment(EnvType.CLIENT)
    protected boolean mouseScrolled(final double mouseX, final double mouseY, final double amount) {
        this.method = "mouseScrolled";

        return this.screen.mouseScrolled(mouseX, mouseY, amount);
    }

    @Environment(EnvType.CLIENT)
    protected boolean keyReleased(final int keyCode, final int scanCode, final int modifiers) {
        this.method = "keyReleased";

        return this.screen.keyReleased(keyCode, scanCode, modifiers);
    }

    @Environment(EnvType.CLIENT)
    protected boolean charTyped(final char chr, final int keyCode) {
        this.method = "charTyped";

        return this.screen.charTyped(chr, keyCode);
    }

    @Environment(EnvType.CLIENT)
    protected void setInitialFocus(@Nullable final Element element) {
        this.method = "setInitialFocus";

        this.screen.setInitialFocus(element);
    }

    @Environment(EnvType.CLIENT)
    protected void focusOn(@Nullable final Element element) {
        this.method = "focusOn";

        this.screen.focusOn(element);
    }

    @Environment(EnvType.CLIENT)
    protected boolean changeFocus(final boolean lookForwards) {
        this.method = "changeFocus";

        return this.screen.changeFocus(lookForwards);
    }

    @Environment(EnvType.CLIENT)
    protected void mouseMoved(final double mouseX, final double mouseY) {
        this.method = "mouseMoved";

        this.screen.mouseMoved(mouseX, mouseY);
    }
}
