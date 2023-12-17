package continued.hideaway.mod.feat.keyboard;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.keyboard.model.KeybindCategoryModel;
import continued.hideaway.mod.feat.keyboard.model.KeybindModel;
import continued.hideaway.mod.feat.ui.InventorySlotsUI;
import continued.hideaway.mod.mixins.ext.ClientPacketListenerAccessor;
import net.minecraft.client.Minecraft;

public class HPKeybinds {
    public HPKeybinds() {
        for (KeybindCategoryModel category : KeybindCategoryModel.values()) {
            KeyBindingRegistryImpl.addCategory(category.translationString);
        }

        for (KeybindModel keybind : KeybindModel.values()) {
            KeyBindingRegistryImpl.registerKeyBinding(keybind.keyMapping);
        }
    }

    public void tick() {
        Minecraft client = HideawayPlus.client();

        while (KeybindModel.LUGGAGE.keyMapping.consumeClick()) InventorySlotsUI.clickSlot(1, client);
        while (KeybindModel.WARDROBE.keyMapping.consumeClick()) InventorySlotsUI.clickSlot(2, client);
        while (KeybindModel.PROFILE.keyMapping.consumeClick()) InventorySlotsUI.clickSlot(3, client);
        while (KeybindModel.FRIENDS.keyMapping.consumeClick()) InventorySlotsUI.clickSlot(4, client);
        while (KeybindModel.JOURNAL.keyMapping.consumeClick()) InventorySlotsUI.clickSlot(43, client);
        while (KeybindModel.PALM_PLATE.keyMapping.consumeClick()) InventorySlotsUI.clickSlot(44, client);
        while (KeybindModel.MAIL.keyMapping.consumeClick()) {
            if (client.getConnection() != null) {
                ((ClientPacketListenerAccessor) client.getConnection()).hp$sendCommand("mail");
            }
        }
    }
}
