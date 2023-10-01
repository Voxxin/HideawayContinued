package continued.hideaway.mod.feat.ext;

import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface AbstractContainerScreenAccessor {
    void hp$slotChange(Slot slot, int slotId, int mouseButton, ClickType type);

    Slot hp$getHoveredSlot();

    AbstractContainerMenu hp$getMenu();
}
