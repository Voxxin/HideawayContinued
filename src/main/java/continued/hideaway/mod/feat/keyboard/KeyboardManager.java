package continued.hideaway.mod.feat.keyboard;

import continued.hideaway.mod.HideawayPlus;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardManager extends GLFWKeyCallback {
    private boolean[] keysPressed = new boolean[26];
    public KeyboardManager() {
    }

    public static boolean isMouseKey(int button) {
        return (GLFW.glfwGetMouseButton(HideawayPlus.client().getWindow().getWindow(), button) == GLFW.GLFW_PRESS);
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW.GLFW_PRESS && GLFW.GLFW_KEY_A <= key && key <= GLFW.GLFW_KEY_Z) {
            int index = key - GLFW.GLFW_KEY_A;
            if (!keysPressed[index]) {
                keysPressed[index] = true;
                System.out.println("Key '" + (char) key + "' is pressed.");
            }
        } else if (action == GLFW.GLFW_RELEASE && GLFW.GLFW_KEY_A <= key && key <= GLFW.GLFW_KEY_Z) {
            int index = key - GLFW.GLFW_KEY_A;
            keysPressed[index] = false;
        }
    }
}
