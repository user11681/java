package user11681.fabriclexicon.item;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import user11681.fabriclexicon.FabricLexicon;
import user11681.fabriclexicon.FabricLexiconScreenHandler;
import user11681.fabriclexicon.component.FabricLexiconComponent;

public class FabricLexiconItem extends Item {
    public static final FabricLexiconItem instance = new FabricLexiconItem(new Settings().maxCount(1));

    public FabricLexiconItem(final Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(final World world, final PlayerEntity user, final Hand hand) {
        if (!world.isClient) {
            final FabricLexiconComponent component = FabricLexicon.componentKey.get(user);

            if (user.isSneaking()) {
                final PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
                buffer.writeBoolean(component.transmute ^= true);

                component.writeSyncPacket(buffer, (ServerPlayerEntity) user);
            } else {
                final MutableText title = new TranslatableText(component.transmute ? "menu.fabriclexicon.transmutation" : "menu.fabriclexicon.knowledge").styled((final Style style) -> style.withColor(TextColor.fromRgb(0xbcb29c)));

                user.openHandledScreen(new SimpleNamedScreenHandlerFactory(FabricLexiconScreenHandler::new, title));
            }
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
