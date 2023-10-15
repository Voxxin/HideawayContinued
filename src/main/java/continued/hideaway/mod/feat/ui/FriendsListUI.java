package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.ext.AbstractContainerScreenAccessor;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
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
        Minecraft client = HideawayPlus.client();

        if (client.screen instanceof ContainerScreen && ((ContainerScreen) client.screen).getMenu().getItems().stream().filter(itemStack -> itemStack.getItem() == Items.PLAYER_HEAD).count() > StaticValues.friendsUsernames.size() - 1)
            StaticValues.friendsCheck = false;

        if (!StaticValues.friendsUUID.contains(client.player.getStringUUID()))
            StaticValues.friendsUUID.add(client.player.getStringUUID());
        if (!StaticValues.friendsUsernames.contains(client.player.getName().getString()))
            StaticValues.friendsUsernames.add(client.player.getName().getString());
        if (StaticValues.friendsCheck) return;
        if (client.screen instanceof ContainerScreen abstractContainerScreen) {
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
                                CompoundTag tag = itemStack.getTag();
                                CompoundTag skull = tag.getCompound("SkullOwner");

                                if (tag.toString().contains("Left click to Accept")) continue;
                                int[] uuidIntArray = skull.getIntArray("Id");
                                String uuid = UUIDUtil.uuidFromIntArray(uuidIntArray).toString();
                                if (!StaticValues.friendsUUID.contains(uuid)) StaticValues.friendsUUID.add(uuid);
                                String name = skull.getString("Name");
                                if (!StaticValues.friendsUsernames.contains(name))
                                    StaticValues.friendsUsernames.add(name);
                            }
                        }
                    })
            );

            if (!hasMorePages) {
                StaticValues.friendsCheck = true;
                client.setScreen(null);
            } else {
                Slot paperSlot = menu.slots.stream().filter(slot -> slot.getItem().getItem() == Items.PAPER && slot.getItem().getTag().getAsString().contains("→")).findFirst().orElse(null);
                ((AbstractContainerScreenAccessor) abstractContainerScreen).hp$slotChange(paperSlot, 0, 0, ClickType.PICKUP);
            }
        } else {
            if (ticker >= 200 && !calledProper) {
                LastSeenMessages.Update messages = new LastSeenMessages.Update(0, new BitSet());
                Instant now = Instant.now();
                client.player.connection.send(
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
