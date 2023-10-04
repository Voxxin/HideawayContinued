package continued.hideaway.mod.feat.keyboard;

import com.mojang.blaze3d.platform.InputConstants;
import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.ModConfigModel;
import continued.hideaway.mod.feat.ui.InventorySlotsUI;
import continued.hideaway.mod.feat.ui.JukeboxUI;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import org.lwjgl.glfw.GLFW;

import java.time.Instant;
import java.util.BitSet;

public class KeyboardManager {
    public static final KeyMapping jukebox = new KeyMapping("key.hp.jukebox", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, "categories.hp");
    public static final KeyMapping autoSell = new KeyMapping("key.hp.autoSell", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_S, "categories.hp");
    public static final KeyMapping luggage = new KeyMapping("key.hp.luggage", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, "categories.hp");
    public static final KeyMapping wardrobe = new KeyMapping("key.hp.wardrobe", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "categories.hp");
    public static final KeyMapping profile = new KeyMapping("key.hp.profile", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, "categories.hp");
    public static final KeyMapping friends = new KeyMapping("key.hp.friends", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_U, "categories.hp");
    public static final KeyMapping journal = new KeyMapping("key.hp.journal", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y, "categories.hp");
    public static final KeyMapping palmPlate = new KeyMapping("key.hp.palm_plate", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, "categories.hp");
    public static final KeyMapping mail = new KeyMapping("key.hp.mail", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, "categories.hp");
    // public static final KeyMapping explore = new KeyMapping("key.hp.explore", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, "categories.hp");
    // public static final KeyMapping debug = new KeyBinding("categories.hplus.debug", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F10,"categories.hplus");
    public KeyboardManager() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (jukebox.consumeClick()) client.setScreen(new JukeboxUI());
            while (luggage.consumeClick()) InventorySlotsUI.clickSlot(1, client);
            while (wardrobe.consumeClick()) InventorySlotsUI.clickSlot(2, client);
            while (profile.consumeClick()) InventorySlotsUI.clickSlot(3, client);
            while (friends.consumeClick()) InventorySlotsUI.clickSlot(4, client);
            while (journal.consumeClick()) InventorySlotsUI.clickSlot(43, client);
            while (palmPlate.consumeClick()) InventorySlotsUI.clickSlot(44, client);
            while (mail.consumeClick()) {
                LastSeenMessages.Update messages = new LastSeenMessages.Update(0, new BitSet());
                Instant now = Instant.now();
                HideawayPlus.client().player.connection.send(new ServerboundChatCommandPacket(
                    "mail",
                    now,
                    0L,
                    ArgumentSignatures.EMPTY,
                    messages
                ));
            }
            while (autoSell.consumeClick()) {
                if (!ModConfigModel.AUTO_SELL.value && HideawayPlus.client().screen != null) {
                    HideawayPlus.shop().tick();
                }
            }
//            while (explore.consumeClick()) {
//                InventorySlotsUI.clickSlot(44, client);
//            }
        });

        KeyBindingHelper.registerKeyBinding(jukebox);
        KeyBindingHelper.registerKeyBinding(autoSell);
        KeyBindingHelper.registerKeyBinding(luggage);
        KeyBindingHelper.registerKeyBinding(wardrobe);
        KeyBindingHelper.registerKeyBinding(profile);
        KeyBindingHelper.registerKeyBinding(friends);
        KeyBindingHelper.registerKeyBinding(journal);
        KeyBindingHelper.registerKeyBinding(palmPlate);
        KeyBindingHelper.registerKeyBinding(mail);
    }

    public static boolean isKeyPressed(int button) {
        return (GLFW.glfwGetMouseButton(HideawayPlus.client().getWindow().getWindow(), button) == GLFW.GLFW_PRESS);
    }
}
