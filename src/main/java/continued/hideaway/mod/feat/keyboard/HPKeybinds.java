package continued.hideaway.mod.feat.keyboard;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.keyboard.model.KeybindCategoryModel;
import continued.hideaway.mod.feat.keyboard.model.KeybindModel;
import continued.hideaway.mod.feat.ui.InventorySlotsUI;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;

import java.time.Instant;
import java.util.BitSet;

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
            LastSeenMessages.Update messages = new LastSeenMessages.Update(0, new BitSet());
            Instant now = Instant.now();
            if (client.player != null) {
                client.player.connection.send(new ServerboundChatCommandPacket(
                        "mail",
                        now,
                        0L,
                        ArgumentSignatures.EMPTY,
                        messages
                ));
            }
        }
    }
}
