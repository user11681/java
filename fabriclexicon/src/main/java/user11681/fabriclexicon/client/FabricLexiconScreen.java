package user11681.fabriclexicon.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import user11681.fabriclexicon.FabricLexicon;
import user11681.fabriclexicon.FabricLexiconScreenHandler;

public class FabricLexiconScreen extends HandledScreen<FabricLexiconScreenHandler> {
    public static final Identifier backgroundTexture = FabricLexicon.id("textures/gui/fabric_lexicon.png");
    public static final Identifier inventoryTexture = FabricLexicon.id("textures/gui/fabric_lexicon_inventory.png");

    public static final int backgroundWidth = 200;
    public static final int backgroundHeight = 205;
    public static final int inventoryWidth = 162;
    public static final int inventoryHeight = 76;

    private static final TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();

    public FabricLexiconScreen(final FabricLexiconScreenHandler handler, final PlayerInventory inventory, final Text title) {
        super(handler, inventory, title);

        super.backgroundWidth = backgroundWidth;
        super.backgroundHeight = backgroundHeight;
        this.titleY += 8;
    }

    @Override
    protected void init() {
        super.init();

        this.titleX = backgroundWidth / 2;
    }

    @Override
    public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(final MatrixStack matrices, final float delta, final int mouseX, final int mouseY) {
        textureManager.bindTexture(backgroundTexture);
        this.drawTexture(matrices, (this.width - backgroundWidth) / 2, (this.height - backgroundHeight) / 2, 0, 0, backgroundWidth, backgroundHeight);

        textureManager.bindTexture(inventoryTexture);
        this.drawTexture(matrices, (this.width - inventoryWidth) / 2 - 3, (this.height - inventoryHeight) / 2 + 49, 0, 0, inventoryWidth, inventoryHeight);
    }

    @Override
    protected void drawForeground(final MatrixStack matrices, final int mouseX, final int mouseY) {
        drawCenteredText(matrices, this.textRenderer, this.title, this.titleX, this.titleY, 0x38342A);
    }
}
