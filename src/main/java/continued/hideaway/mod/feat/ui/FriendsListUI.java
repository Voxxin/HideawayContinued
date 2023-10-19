package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.ext.AbstractContainerScreenAccessor;
import continued.hideaway.mod.mixins.ext.ClientPacketListenerAccessor;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class FriendsListUI {
    public static void tick() {
        if (StaticValues.friendsCheck) return;
        Minecraft client = HideawayPlus.client();

        if (client.screen instanceof ContainerScreen abstractContainerScreen) {
            ChestMenu menu = abstractContainerScreen.getMenu();

            List<ItemStack> items = menu.getItems();
            boolean hasMorePages = false;

            System.out.println("Friends list size: " + StaticValues.friendsUsernames.size());
            for (ItemStack itemStack : items) {
                Item item = itemStack.getItem();
                if (item == Items.PLAYER_HEAD) {
                    CompoundTag tag = itemStack.getTag();
                    if (tag.toString().contains("Left click to Accept")) continue;

                    CompoundTag skull = tag.getCompound("SkullOwner");
                    int[] uuidIntArray = skull.getIntArray("Id");
                    String uuid = UUIDUtil.uuidFromIntArray(uuidIntArray).toString();
                    String name = skull.getString("Name");
                    if (!StaticValues.friendsUUID.contains(uuid)) StaticValues.friendsUUID.add(uuid);
                    if (!StaticValues.friendsUsernames.contains(name)) StaticValues.friendsUsernames.add(name);
                } else if (item == Items.PAPER && itemStack.getTag().getAsString().contains("→")) {
                    hasMorePages = true;
                }
            }

            if (!hasMorePages) {
                StaticValues.friendsCheck = true;
                client.setScreen(null);
            } else {
                Slot paperSlot = menu.slots.stream().filter(slot -> slot.getItem().getItem() == Items.PAPER && slot.getItem().getTag().getAsString().contains("→")).findFirst().orElse(null);
                ((AbstractContainerScreenAccessor) abstractContainerScreen).hp$slotChange(paperSlot, 0, 0, ClickType.PICKUP);
            }
        } else if (client.getConnection() != null) {
            ((ClientPacketListenerAccessor) client.getConnection()).hp$sendCommand("friend");
        }
    }
}
