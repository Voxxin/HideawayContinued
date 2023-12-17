package continued.hideaway.mod.util;

import continued.hideaway.mod.HideawayPlus;
import org.lwjgl.glfw.GLFW;

public class KeyboardManager {
    private boolean rightBtnPressedLastTick = false;

    public boolean isMouseKey(int button) {
        boolean isPressed = GLFW.glfwGetMouseButton(HideawayPlus.client().getWindow().getWindow(), button) == GLFW.GLFW_PRESS;

        boolean result = !isPressed && rightBtnPressedLastTick;
        rightBtnPressedLastTick = isPressed;

        return result;
    }
}