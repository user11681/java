package user11681.fabriclexicon.client;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class OutputSlot extends Slot {
    public OutputSlot(final Inventory inventory, final int index, final int x, final int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(final ItemStack stack) {
        return false;
    }
}
