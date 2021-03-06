package user11681.soulboundarmory.client.gui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import user11681.soulboundarmory.config.Configuration;

public class RGBASlider extends SliderWidget {
    protected static final Configuration.Client.Colors colors = Configuration.instance().client.colors;

    protected final Text text;
    public final int id;

    protected int componentValue;

    public RGBASlider(final int x, final int y, final int width, final int height, final Text text, final double value, final int id) {
        super(x, y, width, height, text, value);

        this.text = text;
        this.id = id;

        this.value = colors.get(id) / 255D;
        this.applyValue();
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(new TranslatableText("%s: %s", this.text, this.componentValue));
    }

    @Override
    public void applyValue() {
        this.componentValue = (int) (0xFF * this.value);

        colors.set(this.id, this.componentValue);
    }

    public void scroll(final double value) {
        this.value = MathHelper.clamp(this.value + value / 255, 0, 1);

        this.applyValue();
        this.updateMessage();
    }
}
