package continued.hideaway.mod.feat.rendering.screen.util;

import continued.hideaway.mod.feat.config.HideawayPlusConfig;
import continued.hideaway.mod.feat.config.model.ModConfigModel;
import continued.hideaway.mod.feat.keyboard.KeyboardManager;
import continued.hideaway.mod.util.WidgetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;


public class ButtonOptionWidget extends AbstractWidget {

    ModConfigModel configModel;
    int[] size;
    int[] pos;

    boolean rightBtnReleased = true;

    public ButtonOptionWidget(int x, int y, int width, int height, ModConfigModel configModel) {
        super(x, y, width, height, Component.translatable(configModel.name));
        this.configModel = configModel;
        this.pos = new int[]{x, y};
        this.size = new int[]{width, height};
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int sizeX = this.size[0] / 2;
        int sizeY = this.size[1] / 2;

        int checkMarkX = sizeX - 3;
        int checkMarkY = sizeY - 3;


        if (KeyboardManager.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_LEFT) && rightBtnReleased && WidgetUtil.overlaying(mouseX, mouseY, this.pos[0] - sizeX, this.pos[1] - sizeY, this.pos[0] + sizeX, this.pos[1] + sizeY)) {
            clicked();
        }
        else if (!KeyboardManager.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_LEFT) && !rightBtnReleased) rightBtnReleased = true;

        // Background
        guiGraphics.fill(this.pos[0] - sizeX, this.pos[1] - sizeY, this.pos[0] + sizeX, this.pos[1] + sizeY, 0x80FFFFFF);


        // Checkmark
        if (this.configModel.value) {
            guiGraphics.fill(this.pos[0] - checkMarkX, this.pos[1] - checkMarkY, this.pos[0] + checkMarkX, this.pos[1] + checkMarkY, 0x8000FF00);
        }

        // Text
        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable(configModel.name), this.pos[0] + sizeX + 10, this.pos[1] - sizeY/2, 0xFFFFFF, true);

    }

    private void clicked() {
        this.playDownSound(Minecraft.getInstance().getSoundManager());
        this.configModel.value = !this.configModel.value;
        HideawayPlusConfig.updateModConfig();
        rightBtnReleased = false;
    }
}
