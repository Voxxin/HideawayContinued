package continued.hideaway.mod.util;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.ext.AbstractContainerScreenAccessor;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;

public class GuiUtils {

    public static ChestMenu getChestMenu() {
        if (HideawayPlus.client().screen instanceof ContainerScreen abstractContainerScreen) {
            return abstractContainerScreen.getMenu();
        }
        return null;
    }

    public static ContainerScreen getContainerScreen() {
        if (HideawayPlus.client().screen instanceof ContainerScreen abstractContainerScreen) {
            return abstractContainerScreen;
        }
        return null;
    }

    public static void pressSlot(ContainerScreen screen, Slot clickSlot) {
        int clickSlotIndex = screen.getMenu().slots.indexOf(clickSlot) == 10 ? 11 : 10;
        ((AbstractContainerScreenAccessor) screen).hp$slotChange(clickSlot, clickSlotIndex, 0, ClickType.PICKUP);
    }

    public static boolean containsNextButton(ChestMenu menu) {
        return menu.getItems().stream().anyMatch(itemStack -> itemStack.getItem() == Items.PAPER && itemStack.getTag().getAsString().contains("→"));
    }

    public static void nextAndPress(ChestMenu menu) {
        Slot nextButton = menu.slots.stream().filter(slot -> slot.getItem().getItem() == Items.PAPER && slot.getItem().getTag().getAsString().contains("→")).findFirst().orElseGet(null);
        if (nextButton != null) pressSlot(getContainerScreen(), nextButton);
    }
}
