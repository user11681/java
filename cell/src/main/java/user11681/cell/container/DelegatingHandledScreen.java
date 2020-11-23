package user11681.cell.container;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class DelegatingHandledScreen extends HandledScreen<Container> {
    final Container container;

    public DelegatingHandledScreen(final Container handler, final PlayerInventory inventory, final Text title) {
        super(handler, inventory, title);

        this.container = handler;
        this.container.screen = this;
    }

    @Override
    protected void drawBackground(final MatrixStack matrices, final float delta, final int mouseX, final int mouseY) {
        this.container.drawBackground(matrices, delta, mouseX, mouseY);
    }

    @Override
    protected void init() {
        if ("init0".equals(this.container.method)) {
            super.init();
        } else {
            this.container.init();
        }
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
        if ("render".equals(this.container.method)) {
            super.render(matrices, mouseX, mouseY, delta);
        } else {
            this.container.render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    protected void drawMouseoverTooltip(final MatrixStack matrices, final int x, final int y) {
        if ("drawMouseoverTooltip".equals(this.container.method)) {
            super.drawMouseoverTooltip(matrices, x, y);
        } else {
            this.container.drawMouseoverTooltip(matrices, x, y);
        }
    }

    @Override
    protected void drawForeground(final MatrixStack matrices, final int mouseX, final int mouseY) {
        if ("drawForeground".equals(this.container.method)) {
            super.drawForeground(matrices, mouseX, mouseY);
        } else {
            this.container.drawForeground(matrices, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if ("mouseClicked".equals(this.container.method)) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        return this.container.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected boolean isClickOutsideBounds(final double mouseX, final double mouseY, final int left, final int top, final int button) {
        if ("isClickOutsideBounds".equals(this.container.method)) {
            return super.isClickOutsideBounds(mouseX, mouseY, left, top, button);
        }

        return this.container.isClickOutsideBounds(mouseX, mouseY, left, top, button);
    }

    @Override
    public boolean mouseDragged(final double mouseX, final double mouseY, final int button, final double deltaX, final double deltaY) {
        if ("mouseDragged".equals(this.container.method)) {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }

        return this.container.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        if ("mouseReleased".equals(this.container.method)) {
            return super.mouseReleased(mouseX, mouseY, button);
        }

        return this.container.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected boolean isPointWithinBounds(final int xPosition, final int yPosition, final int width, final int height, final double pointX, final double pointY) {
        if ("isPointWithinBounds".equals(this.container.method)) {
            return super.isPointWithinBounds(xPosition, yPosition, width, height, pointX, pointY);
        }

        return this.container.isPointWithinBounds(xPosition, yPosition, width, height, pointX, pointY);
    }

    @Override
    protected void onMouseClick(final Slot slot, final int invSlot, final int clickData, final SlotActionType actionType) {
        if ("onMouseClick".equals(this.container.method)) {
            super.onMouseClick(slot, invSlot, clickData, actionType);
        } else {
            this.container.onMouseClick(slot, invSlot, clickData, actionType);
        }
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if ("keyPressed".equals(this.container.method)) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        return this.container.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean handleHotbarKeyPressed(final int keyCode, final int scanCode) {
        if ("handleHotbarKeyPressed".equals(this.container.method)) {
            return super.handleHotbarKeyPressed(keyCode, scanCode);
        }

        return this.container.handleHotbarKeyPressed(keyCode, scanCode);
    }

    @Override
    public void removed() {
        if ("removed".equals(this.container.method)) {
            super.removed();
        } else {
            this.container.removed();
        }
    }

    @Override
    public boolean isPauseScreen() {
        if ("isPauseScreen".equals(this.container.method)) {
            return super.isPauseScreen();
        }

        return this.container.isPauseScreen();
    }

    @Override
    public void tick() {
        if ("tick".equals(this.container.method)) {
            super.tick();
        } else {
            this.container.tick();
        }
    }

    @Override
    public Container getScreenHandler() {
        if ("getScreenHandler".equals(this.container.method)) {
            return super.getScreenHandler();
        }

        return this.container;
    }

    @Override
    public void onClose() {
        if ("onClose".equals(this.container.method)) {
            super.onClose();
        } else {
            this.container.onClose();
        }
    }

    @Override
    public Text getTitle() {
        if ("getTitle".equals(this.container.method)) {
            return super.getTitle();
        }

        return this.container.getTitle();
    }

    @Override
    public String getNarrationMessage() {
        if ("getNarrationMessage".equals(this.container.method)) {
            return super.getNarrationMessage();
        }

        return this.container.getNarrationMessage();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        if ("shouldCloseOnEsc".equals(this.container.method)) {
            return super.shouldCloseOnEsc();
        }

        return this.container.shouldCloseOnEsc();
    }

    @Override
    protected <T extends AbstractButtonWidget> T addButton(final T button) {
        if ("addButton".equals(this.container.method)) {
            return super.addButton(button);
        }

        return this.container.addButton(button);
    }

    @Override
    protected <T extends Element> T addChild(final T child) {
        if ("addChild".equals(this.container.method)) {
            return super.addChild(child);
        }

        return this.container.addChild(child);
    }

    @Override
    protected void renderTooltip(final MatrixStack matrices, final ItemStack stack, final int x, final int y) {
        if ("renderTooltip0".equals(this.container.method)) {
            super.renderTooltip(matrices, stack, x, y);
        } else {
            this.container.renderTooltip(matrices, stack, x, y);
        }
    }

    @Override
    public void method_32634(final MatrixStack matrixStack, final List<Text> list, final Optional<TooltipData> tooltipData, final int i, final int j) {
        if ("method_32634".equals(this.container.method)) {
            super.method_32634(matrixStack, list, tooltipData, i, j);
        } else {
            this.container.method_32634(matrixStack, list, tooltipData, i, j);
        }
    }

    @Override
    public List<Text> getTooltipFromItem(final ItemStack stack) {
        if ("getTooltipFromItem".equals(this.container.method)) {
            return super.getTooltipFromItem(stack);
        }

        return this.container.getTooltipFromItem(stack);
    }

    @Override
    public void renderTooltip(final MatrixStack matrices, final Text text, final int x, final int y) {
        if ("renderTooltip1".equals(this.container.method)) {
            super.renderTooltip(matrices, text, x, y);
        } else {
            this.container.renderTooltip(matrices, text, x, y);
        }
    }

    @Override
    public void renderTooltip(final MatrixStack matrices, final List<Text> lines, final int x, final int y) {
        if ("renderTooltip2".equals(this.container.method)) {
            super.renderTooltip(matrices, lines, x, y);
        } else {
            this.container.renderTooltip(matrices, lines, x, y);
        }
    }

    @Override
    public void renderOrderedTooltip(final MatrixStack matrices, final List<? extends OrderedText> list, final int x, final int y) {
        if ("renderOrderedTooltip".equals(this.container.method)) {
            super.renderOrderedTooltip(matrices, list, x, y);
        } else {
            this.container.renderOrderedTooltip(matrices, list, x, y);
        }
    }

    @Override
    protected void renderTextHoverEffect(final MatrixStack matrices, @Nullable final Style style, final int x, final int y) {
        if ("renderTextHoverEffect".equals(this.container.method)) {
            super.renderTextHoverEffect(matrices, style, x, y);
        } else {
            this.container.renderTextHoverEffect(matrices, style, x, y);
        }
    }

    @Override
    protected void insertText(final String text, final boolean override) {
        if ("insertText".equals(this.container.method)) {
            super.insertText(text, override);
        } else {
            this.container.insertText(text, override);
        }
    }

    @Override
    public boolean handleTextClick(@Nullable final Style style) {
        if ("handleTextClick".equals(this.container.method)) {
            return super.handleTextClick(style);
        }

        return this.container.handleTextClick(style);
    }

    @Override
    public void sendMessage(final String message) {
        if ("sendMessage0".equals(this.container.method)) {
            super.sendMessage(message);
        } else {
            this.container.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(final String message, final boolean toHud) {
        if ("sendMessage1".equals(this.container.method)) {
            super.sendMessage(message, toHud);
        } else {
            this.container.sendMessage(message, toHud);
        }
    }

    @Override
    public void init(final MinecraftClient client, final int width, final int height) {
        if ("init1".equals(this.container.method)) {
            super.init();
        } else {
            this.container.init(client, width, height);
        }
    }

    @Override
    public List<? extends Element> children() {
        if ("children".equals(this.container.method)) {
            return super.children();
        }

        return this.container.children();
    }

    @Override
    public void renderBackground(final MatrixStack matrices) {
        if ("renderBackground0".equals(this.container.method)) {
            super.renderBackground(matrices);
        } else {
            this.container.renderBackground(matrices);
        }
    }

    @Override
    public void renderBackground(final MatrixStack matrices, final int vOffset) {
        if ("renderBackground1".equals(this.container.method)) {
            super.renderBackground(matrices, vOffset);
        } else {
            this.container.renderBackground(matrices, vOffset);
        }
    }

    @Override
    public void renderBackgroundTexture(final int vOffset) {
        if ("renderBackgroundTexture".equals(this.container.method)) {
            super.renderBackgroundTexture(vOffset);
        } else {
            this.container.renderBackgroundTexture(vOffset);
        }
    }

    @Override
    public void resize(final MinecraftClient client, final int width, final int height) {
        if ("resize".equals(this.container.method)) {
            super.resize(client, width, height);
        } else {
            this.container.resize(client, width, height);
        }
    }

    @Override
    protected boolean isValidCharacterForName(final String name, final char character, final int cursorPos) {
        if ("isValidCharacterForName".equals(this.container.method)) {
            return super.isValidCharacterForName(name, character, cursorPos);
        }

        return this.container.isValidCharacterForName(name, character, cursorPos);
    }

    @Override
    public boolean isMouseOver(final double mouseX, final double mouseY) {
        if ("isMouseOver".equals(this.container.method)) {
            return super.isMouseOver(mouseX, mouseY);
        }

        return this.container.isMouseOver(mouseX, mouseY);
    }

    @Override
    public void filesDragged(final List<Path> paths) {
        if ("filesDragged".equals(this.container.method)) {
            super.filesDragged(paths);
        } else {
            this.container.filesDragged(paths);
        }
    }

    @Override
    public Element getFocused() {
        if ("getFocused".equals(this.container.method)) {
            return super.getFocused();
        }

        return this.container.getFocused();
    }

    @Override
    public void setFocused(@Nullable final Element focused) {
        if ("setFocused".equals(this.container.method)) {
            super.setFocused(focused);
        } else {
            this.container.setFocused(focused);
        }
    }

    @Override
    protected void drawHorizontalLine(final MatrixStack matrices, final int x1, final int x2, final int y, final int color) {
        if ("drawHorizontalLine".equals(this.container.method)) {
            super.drawHorizontalLine(matrices, x1, x2, y, color);
        } else {
            this.container.drawHorizontalLine(matrices, x1, x2, y, color);
        }
    }

    @Override
    protected void drawVerticalLine(final MatrixStack matrices, final int x, final int y1, final int y2, final int color) {
        if ("drawVerticalLine".equals(this.container.method)) {
            super.drawVerticalLine(matrices, x, y1, y2, color);
        } else {
            this.container.drawVerticalLine(matrices, x, y1, y2, color);
        }
    }

    @Override
    protected void fillGradient(final MatrixStack matrices, final int xStart, final int yStart, final int xEnd, final int yEnd, final int colorStart, final int colorEnd) {
        if ("fillGradient".equals(this.container.method)) {
            super.fillGradient(matrices, xStart, yStart, xEnd, yEnd, colorStart, colorEnd);
        } else {
            this.container.fillGradient(matrices, xStart, yStart, xEnd, yEnd, colorStart, colorEnd);
        }
    }

    @Override
    public void method_29343(final int i, final int j, final BiConsumer<Integer, Integer> biConsumer) {
        if ("method_29343".equals(this.container.method)) {
            super.method_29343(i, j, biConsumer);
        } else {
            this.container.method_29343(i, j, biConsumer);
        }
    }

    @Override
    public void drawTexture(final MatrixStack matrices, final int x, final int y, final int u, final int v, final int width, final int height) {
        if ("drawTexture".equals(this.container.method)) {
            super.drawTexture(matrices, x, y, u, v, width, height);
        } else {
            this.container.drawTexture(matrices, x, y, u, v, width, height);
        }
    }

    @Override
    public int getZOffset() {
        if ("getZOffset".equals(this.container.method)) {
            return super.getZOffset();
        }

        return this.container.getZOffset();
    }

    @Override
    public void setZOffset(final int zOffset) {
        if ("setZOffset".equals(this.container.method)) {
            super.setZOffset(zOffset);
        } else {
            this.container.setZOffset(zOffset);
        }
    }

    @Override
    public Optional<Element> hoveredElement(final double mouseX, final double mouseY) {
        if ("hoveredElement".equals(this.container.method)) {
            return super.hoveredElement(mouseX, mouseY);
        }

        return this.container.hoveredElement(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(final double mouseX, final double mouseY, final double amount) {
        if ("mouseScrolled".equals(this.container.method)) {
            return super.mouseScrolled(mouseX, mouseY, amount);
        }

        return this.container.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyReleased(final int keyCode, final int scanCode, final int modifiers) {
        if ("keyReleased".equals(this.container.method)) {
            return super.keyReleased(keyCode, scanCode, modifiers);
        }

        return this.container.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(final char character, final int keyCode) {
        if ("charTyped".equals(this.container.method)) {
            return super.charTyped(character, keyCode);
        }

        return this.container.charTyped(character, keyCode);
    }

    @Override
    public void setInitialFocus(@Nullable final Element element) {
        if ("setInitialFocus".equals(this.container.method)) {
            super.setInitialFocus(element);
        } else {
            this.container.setInitialFocus(element);
        }
    }

    @Override
    public void focusOn(@Nullable final Element element) {
        if ("focusOn".equals(this.container.method)) {
            super.focusOn(element);
        } else {
            this.container.focusOn(element);
        }
    }

    @Override
    public boolean changeFocus(final boolean lookForwards) {
        if ("changeFocus".equals(this.container.method)) {
            return super.changeFocus(lookForwards);
        }

        return this.container.changeFocus(lookForwards);
    }

    @Override
    public void mouseMoved(final double mouseX, final double mouseY) {
        if ("mouseMoved".equals(this.container.method)) {
            super.mouseMoved(mouseX, mouseY);
        } else {
            this.container.mouseMoved(mouseX, mouseY);
        }
    }
}
