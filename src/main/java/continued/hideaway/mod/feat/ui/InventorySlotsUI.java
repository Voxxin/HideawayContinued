package continued.hideaway.mod.feat.ui;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;

public class InventorySlotsUI {
    public static void clickSlot(Integer slotNumber, Minecraft client) {
        if (client.player != null && client.getConnection() != null) {
            AbstractContainerMenu abstractContainerMenu = client.player.containerMenu;

//            The idea here was to close the menu (if open) so that you can quickly switch between menus
//            without pushing esc/E and then pushing a hotkey again -- however, the keyboardmanager doesn't register
//            our hotkeys while in a menu
//            client.getConnection().send(
//                    new ServerboundContainerClosePacket(
//                            abstractContainerMenu.containerId
//                    )
//            );
            client.getConnection().send(
                    new ServerboundContainerClickPacket(
                            abstractContainerMenu.containerId,
                            abstractContainerMenu.getStateId(),
                            slotNumber,
                            0,
                            ClickType.PICKUP,
                            client.player.getInventory().getItem(slotNumber),
                            new Int2ObjectOpenHashMap<>()
                    )
            );
        }
    }
}
