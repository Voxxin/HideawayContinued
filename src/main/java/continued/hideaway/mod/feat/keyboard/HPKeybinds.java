package continued.hideaway.mod.feat.keyboard;

import com.mojang.blaze3d.platform.InputConstants;
import continued.hideaway.mod.feat.keyboard.model.KeybindCategoryModel;
import continued.hideaway.mod.feat.keyboard.model.KeybindModel;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class HPKeybinds {
    public HPKeybinds() {
        for (KeybindCategoryModel category : KeybindCategoryModel.values()) {
            KeyBindingRegistryImpl.addCategory(category.translationString);
        }

        for (KeybindModel keybind : KeybindModel.values()) {
            KeyBindingRegistryImpl.registerKeyBinding(keybind.keyMapping);
        }
    }
}
