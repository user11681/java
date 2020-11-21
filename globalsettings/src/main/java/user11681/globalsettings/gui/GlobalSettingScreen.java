package user11681.globalsettings.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import user11681.globalsettings.config.GlobalSettingsConfiguration;

@SuppressWarnings("ConstantConditions")
@Environment(EnvType.CLIENT)
public class GlobalSettingScreen extends Screen {
    private static final GameOptions options = MinecraftClient.getInstance().options;

    private final Screen parent;

    private int startY;

    private TextFieldWidget optionPathField;
    private ButtonWidget apply;
    private ButtonWidget reset;

    public GlobalSettingScreen(final Screen parent) {
        super(new LiteralText("global settings"));

        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        final int startX = this.width / 2 - 150;
        this.startY = this.height / 2;

        this.optionPathField = new TextFieldWidget(this.textRenderer, startX, this.startY, 300, 20, LiteralText.EMPTY);

        this.optionPathField.setMaxLength(Integer.MAX_VALUE);
        this.optionPathField.setText(GlobalSettingsConfiguration.getOptionFile().getPath());

        this.reset = new ButtonWidget(startX, this.startY + 30, 100, 20, new TranslatableText("globalsettings.reset"), (final ButtonWidget reset) -> {
            this.optionPathField.setText(GlobalSettingsConfiguration.getOptionFile().getPath());

            this.setActive(false);
        });

        this.apply = new ButtonWidget(startX + 200, this.startY + 30, 100, 20, new TranslatableText("globalsettings.apply"), (final ButtonWidget apply) -> {
            GlobalSettingsConfiguration.setOptionPath(this.optionPathField.getText());

            if (GlobalSettingsConfiguration.getOptionFile().exists()) {
                options.load();

                this.client.onResolutionChanged();
            } else {
                GlobalSettingsConfiguration.ensureFileExists();

                options.write();
            }

            this.setActive(false);
        });

        this.setActive(false);

        this.addButton(this.optionPathField);
        this.addButton(this.apply);
        this.addButton(this.reset);
        this.addButton(new ButtonWidget(this.width / 2 - 50, this.height - 30, 100, 20, new TranslatableText("mco.selectServer.close"), (final ButtonWidget button) -> this.onClose()));
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, this.textRenderer, new TranslatableText("globalsettings.path"), this.width / 2, this.startY - 10, 0xFFFFFF);
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        final boolean handled = super.keyPressed(keyCode, scanCode, modifiers);

        this.setActive(!this.optionPathField.getText().equals(GlobalSettingsConfiguration.getOptionFile().getPath()));

        return handled;
    }

    @Override
    public boolean charTyped(final char character, final int keyCode) {
        final boolean handled =  super.charTyped(character, keyCode);

        this.setActive(!this.optionPathField.getText().equals(GlobalSettingsConfiguration.getOptionFile().getPath()));

        return handled;
    }

    @Override
    public void onClose() {
        GlobalSettingsConfiguration.setOptionPath(this.optionPathField.getText());

        this.client.openScreen(this.parent);
    }

    public void setActive(final boolean active) {
        this.reset.active = this.apply.active = active;
    }
}
