package continued.hideaway.mod.feat.keyboard.model;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public enum KeybindModel {
    JUKEBOX(
            "key.hp.jukebox",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            KeybindCategoryModel.HIDEAWAY_PLUS.translationString
    ),
    AUTO_SELL(
            "key.hp.autoSell",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_S,
            KeybindCategoryModel.HIDEAWAY_PLUS.translationString
    ),
    LUGGAGE(
            "key.hp.luggage",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            KeybindCategoryModel.HIDEAWAY_PLUS.translationString
    ),
    WARDROBE(
            "key.hp.wardrobe",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            KeybindCategoryModel.HIDEAWAY_PLUS.translationString
    ),
    PROFILE(
            "key.hp.profile",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            KeybindCategoryModel.HIDEAWAY_PLUS.translationString
    ),
    FRIENDS(
            "key.hp.friends",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            KeybindCategoryModel.HIDEAWAY_PLUS.translationString
    ),
    JOURNAL(
            "key.hp.journal",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            KeybindCategoryModel.HIDEAWAY_PLUS.translationString
    ),
    PALM_PLATE(
            "key.hp.palm_plate",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            KeybindCategoryModel.HIDEAWAY_PLUS.translationString
    ),
    MAIL(
            "key.hp.mail",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            KeybindCategoryModel.HIDEAWAY_PLUS.translationString
    );

    public final String translationString;
    public final InputConstants.Type type;
    public final int keyCode;
    public final String category;
    public final KeyMapping keyMapping;

    KeybindModel(String translationString, InputConstants.Type type, int keyCode, String category) {
        this.translationString = translationString;
        this.type = type;
        this.keyCode = keyCode;
        this.category = category;
        this.keyMapping = new KeyMapping(translationString, type, keyCode, category);
    }
}
