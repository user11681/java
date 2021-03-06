package user11681.soulboundarmory.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import user11681.soulboundarmory.SoulboundArmoryClient;
import user11681.soulboundarmory.client.i18n.Translations;
import user11681.soulboundarmory.component.soulbound.item.ItemStorage;
import user11681.soulboundarmory.component.soulbound.item.StorageType;
import user11681.soulboundarmory.config.Configuration;
import user11681.soulboundarmory.item.SoulboundItem;
import user11681.spun.client.gui.screen.SpunScreen;
import user11681.spun.client.gui.widget.scalable.ScalableWidget;

import static user11681.soulboundarmory.component.statistics.StatisticType.EXPERIENCE;
import static user11681.soulboundarmory.component.statistics.StatisticType.LEVEL;

@Environment(EnvType.CLIENT)
public class ExperienceBarOverlay extends SpunScreen {
    protected static final Configuration.Client configuration = Configuration.instance().client;
    protected static final Configuration.Client.Colors colors = configuration.colors;

    protected final ScalableWidget widget = ExperienceBarWidgetSupplier.COLORED_EXPERIENCE_BAR.get();

    protected ItemStack itemStack;

    protected ItemStorage<?> component;
    protected int row;
    protected int length;

    public ExperienceBarOverlay(final ItemStack itemStack) {
        this();

        this.update(itemStack);
    }

    public ExperienceBarOverlay(final ItemStorage<?> component) {
        this();

        this.update(component);

        this.component = component;
    }

    public ExperienceBarOverlay() {
        super(null);
    }

    public void setData(final int row, final int length) {
        this.row = row;
        this.length = length;
    }

    public void drawTooltip(final int tooltipX, final int tooltipY, final ItemStack itemStack) {
        if (this.update(itemStack)) {
            final int x = tooltipX + 4;
            final int y = tooltipY + this.row * 10;

            this.render(x, y, this.length);
        }
    }

    public boolean update(final ItemStack itemStack) {
        if (this.update(ItemStorage.get(SoulboundArmoryClient.getPlayer(), itemStack.getItem()))) {
            if (itemStack.getItem() instanceof SoulboundItem && this.itemStack != itemStack) {
                this.itemStack = itemStack;
            }
        }

        return false;
    }

    public boolean update(final ItemStorage<?> component) {
        if (component != null) {
            this.component = component;
        }

        return this.component != null;
    }

    public void update() {

    }

    public boolean render() {
        final PlayerEntity player = SoulboundArmoryClient.getPlayer();
        final Window window = CLIENT.getWindow();

        for (final ItemStack itemStack : player.getItemsHand()) {
            if (this.update(StorageType.get(player, itemStack.getItem()))) {
                this.render((window.getScaledWidth() - 182) / 2, window.getScaledHeight() - 29, 182);

                return true;
            }
        }

        return false;
    }

    public void render(final int x, final int y, final int width) {
        if (colors.alpha > 3) {
            final MatrixStack stack = new MatrixStack();
            final Color color = new Color(colors.red, colors.green, colors.blue, colors.alpha);
            final float[] components = color.getComponents(null);
            Style style = configuration.style;

            if (style == null) {
                style = Style.EXPERIENCE;
            }

            final float ratio = (float) this.component.getDatum(EXPERIENCE) / this.component.getNextLevelXP();
            final float effectiveWidth = ratio * width;
            final int middleU = (int) Math.min(4, effectiveWidth);

            this.widget.color4f(components[0], components[1], components[2], components[3]);

            this.widget.renderButton(stack, x, y, 0, style.v, 4, 177, 182, width, 5);
            this.widget.renderButton(stack, x, y, 0, style.v + 5, middleU, effectiveWidth < 4 ? middleU : (int) (ratio * 177), (int) (ratio * 182), this.component.canLevelUp()
                    ? Math.min(width, (int) (ratio * width))
                    : width, 5);

            final int level = this.component.getDatum(LEVEL);

            if (level > 0) {
                final String levelString = String.format("%d", level);
                final int levelX = x + (width - this.textRenderer.getWidth(levelString)) / 2;
                final int levelY = y - 6;

                this.textRenderer.draw(stack, levelString, levelX + 1, levelY, 0);
                this.textRenderer.draw(stack, levelString, levelX - 1, levelY, 0);
                this.textRenderer.draw(stack, levelString, levelX, levelY + 1, 0);
                this.textRenderer.draw(stack, levelString, levelX, levelY - 1, 0);
                this.textRenderer.draw(stack, levelString, levelX, levelY, color.getRGB());
            }

            RenderSystem.disableLighting();
        }
    }

    public enum Style {
        EXPERIENCE(64, Translations.EXPERIENCE_STYLE),
        BOSS(74, Translations.BOSS_STYLE),
        HORSE(84, Translations.HORSE_STYLE);

        public static final List<Style> STYLES = new ArrayList<>(Arrays.asList(Style.values()));
        public static final int AMOUNT = STYLES.size();

        public final int v;
        protected final Text text;

        Style(final int v, final Text text) {
            this.v = v;
            this.text = text;
        }

        public Text getText() {
            return this.text;
        }
    }
}
