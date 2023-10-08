package continued.hideaway.mod.feat.rendering.screen.widget;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.keyboard.KeyboardManager;
import continued.hideaway.mod.util.WidgetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class ButtonScreenWidget extends AbstractWidget {
    Screen screenEvent;
    Component buttonName;
    int posX = 0;
    int posY = 0;
    int width = 0;
    int height = 0;

    public ButtonScreenWidget(int x, int y, int width, int height, String translationKey, Screen screenToOpen) {
        super(x, y, width, height, Component.translatable(translationKey));

        this.screenEvent = screenToOpen;
        this.buttonName = Component.translatable(translationKey);
        this.posX = x;
        this.posY = y;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.USAGE, this.buttonName);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int sizeX = this.width / 2;
        int sizeY = this.height / 2;

        if (KeyboardManager.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_LEFT) && WidgetUtil.overlaying(mouseX, mouseY, posX - sizeX, posY - sizeY, posX + sizeX, posY + sizeY)) {
            clicked();
        }

        guiGraphics.fill(posX - sizeX, posY - sizeY, posX + sizeX, posY + sizeY, 0x80FFFFFF);
    }

    private void clicked() {
        this.playDownSound(Minecraft.getInstance().getSoundManager());
        HideawayPlus.client().setScreen(this.screenEvent);
    }
}
