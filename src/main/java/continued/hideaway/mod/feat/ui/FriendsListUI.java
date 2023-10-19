package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.ext.AbstractContainerScreenAccessor;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FriendsListUI {
    private static ChestMenu oldMenu = null;
    private static int ticker = 175;
    private static boolean calledProper = false;

    public static void tick() {
        if (HideawayPlus.client().screen != null && HideawayPlus.client().screen instanceof ContainerScreen containerScreen && containerScreen.getMenu().getItems().stream().filter(itemStack -> itemStack.getItem() == Items.PLAYER_HEAD).count() > StaticValues.friendsUsernames.size() - 1)
            StaticValues.friendsCheck = false;

        if (!StaticValues.friendsUUID.contains(HideawayPlus.client().player.getStringUUID()))
            StaticValues.friendsUUID.add(HideawayPlus.client().player.getStringUUID());
        if (!StaticValues.friendsUsernames.contains(HideawayPlus.client().player.getName().getString()))
            StaticValues.friendsUsernames.add(HideawayPlus.client().player.getName().getString());
        if (StaticValues.friendsCheck) return;
        if (HideawayPlus.client().screen instanceof ContainerScreen abstractContainerScreen) {
            ChestMenu menu = abstractContainerScreen.getMenu();
            calledProper = true;
            if (oldMenu != null && oldMenu == menu) return;
            oldMenu = menu;

            List<ItemStack> allItems = new ArrayList<>(menu.getItems());
            boolean hasMorePages = menu.getItems().stream().anyMatch(itemStack -> itemStack.getItem() == Items.PAPER && itemStack.getTag().getAsString().contains("→"));

            HideawayPlus.lifecycle().addAsync(
                    "friendsCheck",
                    CompletableFuture.runAsync(() -> {
                        System.out.println("Friends list size: " + StaticValues.friendsUsernames.size());
                        List<ItemStack> newAllItems = new ArrayList<>(allItems);
                        for (ItemStack itemStack : newAllItems) {
                            if (itemStack.getItem() == Items.PLAYER_HEAD) {
                                if (itemStack.getTag().toString().contains("Left click to Accept")) continue;
                                int[] uuidIntArray = itemStack.getTag().getCompound("SkullOwner").getIntArray("Id");
                                String uuid = UUIDUtil.uuidFromIntArray(uuidIntArray).toString();
                                if (!StaticValues.friendsUUID.contains(uuid)) StaticValues.friendsUUID.add(uuid);
                                String name = itemStack.getTag().getCompound("SkullOwner").getString("Name");
                                if (!StaticValues.friendsUsernames.contains(name))
                                    StaticValues.friendsUsernames.add(name);
                            }
                        }
                    })
            );

            if (!hasMorePages) {
                StaticValues.friendsCheck = true;
                HideawayPlus.client().setScreen(null);
            } else {
                Slot paperSlot = menu.slots.stream().filter(slot -> slot.getItem().getItem() == Items.PAPER && slot.getItem().getTag().getAsString().contains("→")).findFirst().orElse(null);
                ((AbstractContainerScreenAccessor) abstractContainerScreen).hp$slotChange(paperSlot, 0, 0, ClickType.PICKUP);
            }
        } else {
            if (ticker >= 200 && !calledProper) {
                LastSeenMessages.Update messages = new LastSeenMessages.Update(0, new BitSet());

                Instant now = Instant.now();
                HideawayPlus.client().player.connection.send(
                        new ServerboundChatCommandPacket(
                                "friend",
                                now,
                                0L,
                                ArgumentSignatures.EMPTY,
                                messages)
                );
            } else {
                ticker++;
                calledProper = false;
            }

        }
    }
}
