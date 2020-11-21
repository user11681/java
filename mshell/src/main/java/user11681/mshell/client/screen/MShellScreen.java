package user11681.mshell.client.screen;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import user11681.cell.client.gui.screen.CellScreen;
import user11681.cell.client.gui.widget.scalable.ScalableWidget;
import user11681.cell.client.gui.widget.scalable.ScalableWidgets;
import user11681.mshell.shell.MEngine;

@Environment(EnvType.CLIENT)
public class MShellScreen extends CellScreen {
    protected static final Int2IntMap shifted = new Int2IntOpenHashMap() {{
        put('`', '~');
        put('1', '!');
        put('2', '@');
        put('3', '#');
        put('4', '$');
        put('5', '%');
        put('6', '^');
        put('7', '&');
        put('8', '*');
        put('9', '(');
        put('0', ')');
        put('-', '_');
        put('=', '+');
        put('<', '>');
        put(',', '<');
        put('.', '>');
        put('/', '?');
        put(';', ':');
        put('\'', '"');
        put('\\', '|');
        put('[', '{');
        put(']', '}');
    }};

    protected final StringBuffer text;

    protected List<StringVisitable> lines;

    protected int centerX;
    protected int centerY;
    protected int endX;
    protected int endY;
    protected int insideX;
    protected int insideY;
    protected int endInsideX;
    protected int endInsideY;
    protected int insideWidth;
    protected int insideHeight;
    protected int textX;
    protected int textY;

    protected int caretX;
    protected int caretY;

    protected int index;
    protected int column;

    protected ScalableWidget window;

    public MShellScreen() {
        super(LiteralText.EMPTY);

        this.text = new StringBuffer();
        this.lines = new ArrayList<>();
    }

    @Override
    protected void init() {
        super.init();

        this.centerX = this.width / 2;
        this.centerY = this.height / 2;

        this.window = ScalableWidgets.window()
            .width((int) (this.width * 0.6F))
            .height((int) (this.height * 0.6F))
            .x(this.centerX)
            .y(this.window.y = this.centerY);

        this.add(this.window);

        this.endX = this.window.endX();
        this.endY = this.window.endY();
        this.insideX = this.window.x + 8;
        this.insideY = this.window.y + 18;
        this.endInsideX = this.endX - 8;
        this.endInsideY = this.endY - 9;
        this.insideWidth = this.endInsideX - this.insideX;
        this.insideHeight = this.endInsideY - this.insideY;
        this.textX = this.insideX + 2;
        this.textY = this.insideY + 2;

        this.updateCaret();
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);

        fill(matrices, this.insideX, this.insideY, this.endInsideX, this.endInsideY, 0xFF000000);

        this.renderText(matrices);
        this.renderCaret(matrices);
    }

    protected void renderText(final MatrixStack matrices) {
        final List<StringVisitable> lines = this.lines;

        for (int i = 0, size = lines.size(); i < size; i++) {
            final StringVisitable line = lines.get(i);

            this.textRenderer.draw(matrices, line.getString(), this.textX, this.getY(i), 0xFFFFFF);
        }
    }

    protected void renderCaret(final MatrixStack matrices) {
        this.textRenderer.draw(matrices, Caret.UNDERSCORE.character, this.caretX, this.caretY, 0xFFFFFF);
    }

    protected int getY(final int line) {
        return this.textY + line * (this.textRenderer.fontHeight + 3);
    }

    @Override
    public boolean keyPressed(int keyCode, final int scanCode, final int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        switch (keyCode) {
            case GLFW.GLFW_KEY_ENTER:
                if ((modifiers & GLFW.GLFW_MOD_CONTROL) == 0) {
                    this.text.append('\n');
                } else {
                    MEngine.tryExecute(this.text.toString());
                }

                break;
            case GLFW.GLFW_KEY_BACKSPACE:
                if (this.index > 0) {
                    if ((modifiers & GLFW.GLFW_MOD_CONTROL) == 0) {
                        this.text.deleteCharAt(--this.index);
                    } else {
                        final String substring = this.text.substring(0, this.index);
                        int start = Math.max(substring.lastIndexOf(' '), substring.lastIndexOf('\n')) + 1;

                        if (start == this.index) {
                            for (int i = start; i > 0; --i) {
                                if (!substring.substring(i - 1, i).matches("[ \\n]")) {
                                    start = i;

                                    break;
                                }
                            }
                        }

                        this.text.delete(start, this.index);
                        this.index = start;
                    }
                }

                break;
            case GLFW.GLFW_KEY_DELETE:
                if ((modifiers & GLFW.GLFW_MOD_CONTROL) == 0) {
                    this.text.delete(this.index, this.index + 1);
                } else if (this.index < this.lines.get(this.column).getString().length()) {
                    final String substring = this.text.substring(this.index);
                    int end = Math.min(substring.indexOf(' '), substring.indexOf('\n'));

                    if (end < 0) {
                        end = this.text.length();
                    }

                    this.text.delete(this.index, end);
                }

                break;
            case GLFW.GLFW_KEY_LEFT_SHIFT:
            case GLFW.GLFW_KEY_RIGHT_SHIFT:
            case GLFW.GLFW_KEY_LEFT_CONTROL:
            case GLFW.GLFW_KEY_RIGHT_CONTROL:
            case GLFW.GLFW_KEY_LEFT_SUPER:
            case GLFW.GLFW_KEY_RIGHT_SUPER:
            case GLFW.GLFW_KEY_LEFT_ALT:
            case GLFW.GLFW_KEY_RIGHT_ALT:
                break;
            case GLFW.GLFW_KEY_LEFT:
                if (this.index > 0) {
                    --this.index;
                }

                break;
            case GLFW.GLFW_KEY_RIGHT:
                if (this.index < this.lines.get(this.column).getString().length()) {
                    ++this.index;
                }

                break;
            case GLFW.GLFW_KEY_UP:
                if (this.column < this.lines.size()) {
                    ++this.column;
                }

                break;
            case GLFW.GLFW_KEY_DOWN:
                if (this.column > 0) {
                    --this.column;
                }

                break;
            default:
                if ((modifiers & GLFW.GLFW_MOD_SHIFT) != 0) {
                    keyCode = shifted.getOrDefault(keyCode, keyCode);
                } else if (keyCode >= 64 && keyCode < 90) {
                    keyCode += 32;
                }

                this.text.insert(this.index++, (char) keyCode);
        }

        this.lines = this.textRenderer.getTextHandler().wrapLines(this.text.toString(), this.insideWidth - 4, Style.EMPTY);
        this.updateCaret();

        return false;
    }

    protected void updateCaret() {
        final int size = this.lines.size();

        if (size == 0) {
            this.caretX = this.textX;
            this.caretY = this.textY;
        } else {
            this.caretX = this.textX + this.textRenderer.getWidth(this.lines.get(size - 1).getString().substring(0, this.index));
            this.caretY = this.getY(this.column);
        }
    }

    protected boolean isEmpty() {
        return this.text.length() == 0;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public enum Caret {
        UNDERSCORE("_");

        final Text character;

        Caret(final String character) {
            this(new LiteralText(character));
        }

        Caret(final Text character) {
            this.character = character;
        }
    }
}
