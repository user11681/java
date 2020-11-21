package user11681.fabriclexicon.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

public class FabricLexiconComponent implements AutoSyncedComponent {
    public final PlayerEntity player;

    public boolean transmute;

    public FabricLexiconComponent(final PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void readFromNbt(final CompoundTag tag) {
        this.transmute = tag.getBoolean("transmute");
    }

    @Override
    public void writeToNbt(final CompoundTag tag) {
        tag.putBoolean("transmute", this.transmute);
    }
}
