package continued.hideaway.mod.feat.shop;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.model.GeneralConfigModel;
import continued.hideaway.mod.feat.ext.AbstractContainerScreenAccessor;
import continued.hideaway.mod.feat.keyboard.model.KeybindModel;
import continued.hideaway.mod.feat.ui.FriendsListUI;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Shop {

    private static boolean fill;
    public String oldShopName = null;
    public void tick() {
        Minecraft client = HideawayPlus.client();
        if (client.screen == null || !(client.screen instanceof ContainerScreen containerScreen)) return;

        String shopName = getShopName(client.screen);

        if (shopName == null) {
            oldShopName = null;
            return;
        }

        if (KeybindModel.AUTO_SELL.isDown()) {
            fill = true;
        }

        if (("fruit".equals(shopName) || "fish".equals(shopName)) && (GeneralConfigModel.AUTO_SELL.asBoolean() || fill)) {
            if (oldShopName != null && !oldShopName.equals(shopName)) StaticValues.shopIterationNum = 0;
            oldShopName = shopName;
            List<Slot> emptyChestSlots = new ArrayList<>();
            List<Slot> playerEmptySlots = new ArrayList<>();
            ChestMenu chestMenu = containerScreen.getMenu();

            for (Slot slot : chestMenu.slots.subList(0, chestMenu.slots.size() - 3)) {
                if (slot.getItem().getItem() != Items.AIR) {
                    if (slot.container instanceof Inventory) {
                        playerEmptySlots.add(slot);
                    }
                } else if (!(slot.container instanceof Inventory)) {
                    emptyChestSlots.add(slot);
                }
            }

            for (int i = StaticValues.shopIterationNum; i < playerEmptySlots.size() && !emptyChestSlots.isEmpty() && !StaticValues.shopScreenWasFilled; i++) {
                Slot playerSlot = playerEmptySlots.get(i);

                ((AbstractContainerScreenAccessor)containerScreen).hp$slotChange(playerSlot, emptyChestSlots.get(0).index, 0, ClickType.QUICK_MOVE);

                Iterator<Slot> chestSlotIterator = emptyChestSlots.iterator();
                while (chestSlotIterator.hasNext()) {
                    Slot chestSlot = chestSlotIterator.next();
                    if (chestSlot.getItem().getItem() != Items.AIR) {
                        chestSlotIterator.remove();
                        break;
                    }
                }

                if (emptyChestSlots.isEmpty()) StaticValues.shopIterationNum++;
                if (i == playerEmptySlots.size() -1) {StaticValues.shopScreenWasFilled = true; fill = false;}
                if (StaticValues.shopIterationNum >= playerEmptySlots.size()) {StaticValues.shopScreenWasFilled = true; fill = false;}
            }
        }
    }

    private String getShopName(Screen menu) {
        ChestMenu screen = ((ContainerScreen) menu).getMenu();
        String screenName = menu.getTitle().getString();
        if (screenName.contains("\uE00C") || screenName.contains("\uE010")) { FriendsListUI.tick(); return null; }


        for (ItemStack itemStack : screen.getItems()) {
            if (!itemStack.hasTag()) continue;
            String nbtData = itemStack.getTag().getAsString();

            if (nbtData.contains("buy")) {
                if (nbtData.contains("The Marmoset Monkey Brothers")) return "fruit";
                if (nbtData.contains("Bill Beaks")) return "fish";
            } else if (nbtData.contains("same rarity")) { return "trader"; }
        }

        return null;
    }
}
