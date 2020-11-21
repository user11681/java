package user11681.fabriclexicon;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import user11681.fabriclexicon.client.FabricLexiconScreen;

public class FabricLexiconScreenHandler extends ScreenHandler {
    public static final ScreenHandlerType<FabricLexiconScreenHandler> type = ScreenHandlerRegistry.registerSimple(FabricLexicon.id("fabric_lexicon"), FabricLexiconScreenHandler::new);

    public final PlayerEntity player;
    public final PlayerInventory playerInventory;
    public final SimpleInventory input;
    public final SimpleInventory output;

    public FabricLexiconScreenHandler(final int syncId, final PlayerInventory playerInventory) {
        this(syncId, playerInventory, playerInventory.player);
    }

    public FabricLexiconScreenHandler(final int syncId, final PlayerInventory playerInventory, final PlayerEntity player) {
        super(type, syncId);

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, i % 9 * 18 + 17, 172));
        }

        for (int i = 9; i < 36; i++) {
            this.addSlot(new Slot(playerInventory, i, i % 9 * 18 + 17, i / 9 * 18 + 96));
        }

        this.player = player;
        this.playerInventory = playerInventory;

        if (FabricLexicon.componentKey.get(this.player).transmute) {
            this.input = new SimpleInventory(1);
            this.output = new SimpleInventory(2);
            this.addSlot(new Slot(this.input, 36, FabricLexiconScreen.backgroundWidth / 2 - 8, 48));
        } else {
            this.input = new SimpleInventory(1);
            this.output = new SimpleInventory(1);
        }
    }

    @Override
    public boolean canUse(final PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferSlot(final PlayerEntity player, final int index) {
        return this.getSlot(index).getStack();
    }
}
