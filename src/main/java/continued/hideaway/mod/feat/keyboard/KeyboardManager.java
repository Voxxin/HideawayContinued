package continued.hideaway.mod.feat.keyboard;

import continued.hideaway.mod.HideawayPlus;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.screens.ChatScreen;
import org.lwjgl.glfw.GLFW;

import java.util.Locale;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardManager {

    private static String oldKey = "";

    private static boolean isKeyDown(int key) {
        return GLFW.glfwGetKey(HideawayPlus.client().getWindow().getWindow(), key) == GLFW_PRESS;
    }
    public static boolean isMouseKey(int button) {
        return (GLFW.glfwGetMouseButton(HideawayPlus.client().getWindow().getWindow(), button) == GLFW.GLFW_PRESS);
    }

    public static String parseKeyInput(String newString, String text) {
        String L_Control = "Left Control";
        String R_Control = "Right Control";
        String L_Shift = "Left Shift";
        String R_Shift = "Right Shift";
        String Backspace = "Backspace";
        String Space = "Space";
        String Delete = "Delete";

        if (newString == null) return text;

        if (L_Control.equals(oldKey) && (newString.equals("V") || newString.equals("C") || newString.equals(Backspace))) {
            if (newString.equals("V")) {
                text += HideawayPlus.client().keyboardHandler.getClipboard();
            } else if (newString.equals("C")){
                HideawayPlus.client().keyboardHandler.setClipboard(text);
            } else if (newString.equals(Backspace)) {
                text = "";
            }
        } else if (newString.equals(Backspace)) {
            if (text.length() > 0) {
                text = text.substring(0, text.length() - 1);
            }
        } else if (newString.equals(Space)) {
            text += " ";
        } else if (newString.matches("[a-zA-Z0-9]") || newString.matches("(\\:\\\\\\/\\?\\-\\=\\+\\(\\)\\&\\%\\!\\@\\#\\$\\\\\\^\\[\\]\\\\\\:\\,\\.\\>\\<)")) {
            newString = newString.toLowerCase(Locale.ROOT);
            if (isKeyDown(GLFW_KEY_LEFT_SHIFT)) newString = newString.toUpperCase(Locale.ROOT);

            text += newString;
        }

        oldKey = newString;
        return text;
    }
}
