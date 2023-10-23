package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.ext.AbstractContainerScreenAccessor;
import continued.hideaway.mod.mixins.ext.ClientPacketListenerAccessor;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FriendsListUI {
    private static ChestMenu oldMenu = null;
    public static void tick() {
        if (StaticValues.friendsCheck) return;
        Minecraft client = HideawayPlus.client();

        if (client.screen instanceof ContainerScreen abstractContainerScreen) {
            ChestMenu menu = abstractContainerScreen.getMenu();
            if (oldMenu != null && oldMenu == menu) return;
            oldMenu = menu;

            Slot nextPage = null;
            for (Slot slot : menu.slots) {
                ItemStack itemStack = slot.getItem();
                CompoundTag tag = itemStack.getTag();

                if (tag != null) {
                    Item item = itemStack.getItem();
                    String tagStr = tag.toString();
                    if (item == Items.PLAYER_HEAD && !tagStr.contains("Left click to Accept")) {
                        CompoundTag skull = tag.getCompound("SkullOwner");
                        String name = skull.getString("Name");
                        if (!StaticValues.friends.contains(name)) StaticValues.friends.add(name);
                    } else if (item == Items.PAPER && tagStr.contains("→")) {
                        nextPage = slot;
                    }
                }
            }

            if (nextPage == null) {
                client.setScreen(null);
                StaticValues.friendsCheck = true;
                System.out.println(StaticValues.friends);
            } else {
                ((AbstractContainerScreenAccessor) abstractContainerScreen).hp$slotChange(nextPage, 0, 0, ClickType.PICKUP);
            }
        } else if (client.getConnection() != null) {
            ((ClientPacketListenerAccessor) client.getConnection()).hp$sendCommand("friend");
        }
    }
}