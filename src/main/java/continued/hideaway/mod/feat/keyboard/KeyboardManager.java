package continued.hideaway.mod.feat.keyboard;

import com.mojang.blaze3d.platform.InputConstants;
import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.ModConfigModel;
import continued.hideaway.mod.feat.ui.InventorySlotsUI;
import continued.hideaway.mod.feat.ui.JukeboxUI;
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


    public static final KeyMapping[] keyMappings = new KeyMapping[]{
            jukebox,
            autoSell,
            luggage,
            wardrobe,
            profile,
            friends,
            journal,
            palmPlate,
            mail
    };

    public KeyboardManager() {
        KeyBindingRegistryImpl.addCategory("categories.hp");
        for (KeyMapping keyMapping : keyMappings) {
            KeyBindingRegistryImpl.registerKeyBinding(keyMapping);
        }
    }


    public static boolean isKeyPressed(int button) {
        return (GLFW.glfwGetMouseButton(HideawayPlus.client().getWindow().getWindow(), button) == GLFW.GLFW_PRESS);
    }
}
