package continued.hideaway.mod.util;

public class WidgetUtil {
    public static boolean overlaying(int mouseX, int mouseY, int aX, int aY, int bX, int bY) {
        return mouseX >= aX && mouseX <= bX && mouseY >= aY && mouseY <= bY;
    }
}