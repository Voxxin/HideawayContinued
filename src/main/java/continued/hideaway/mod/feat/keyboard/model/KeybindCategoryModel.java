package continued.hideaway.mod.feat.keyboard.model;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public enum KeybindCategoryModel {
    HIDEAWAY_PLUS (
            "categories.hp"
    );

    public final String translationString;

    KeybindCategoryModel(String translationString) {
        this.translationString = translationString;
    }
}
