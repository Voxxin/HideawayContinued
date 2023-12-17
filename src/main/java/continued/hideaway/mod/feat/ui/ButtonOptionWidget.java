package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.util.KeyboardManager;
import continued.hideaway.mod.util.WidgetUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class ButtonOptionWidget extends AbstractWidget {
    private final ButtonClickAction action;
    public final Component title;
    private final int[] pos;
    private final int[] size;
    private final ButtonOptionType buttonOptionType;

    private final ResourceLocation texture;
    private final ResourceLocation overlayTexture;

    private boolean buttonWasClicked = false;
    private final KeyboardManager KEYBOARD_MANAGER = new KeyboardManager();

    public ButtonOptionWidget(int x, int y, int width, int height, Component title, ButtonClickAction action, ButtonOptionType buttonOptionType) {
        super(x, y, width, height, title);
        this.action = action;
        this.title = title;
        this.pos = new int[]{x, y};
        this.size = new int[]{width, height};
        if (buttonOptionType == null) {
            this.buttonOptionType = null;
            this.texture = null;
            this.overlayTexture = null;
        } else {
            this.buttonOptionType = buttonOptionType;
            this.texture = this.buttonOptionType.state() ? this.buttonOptionType.type().recourseLocation[1] : this.buttonOptionType.type().recourseLocation[0];
            this.overlayTexture = this.buttonOptionType.state() ? this.buttonOptionType.type().recourseLocationOverlay[1] : this.buttonOptionType.type().recourseLocationOverlay[0];
        }
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float partialTick) {
        int sizeX = this.size[0] / 2;
        int sizeY = this.size[1] / 2;
        boolean overlaying = WidgetUtil.overlaying(mouseX, mouseY, this.pos[0] - sizeX, this.pos[1] - sizeY, this.pos[0] + sizeX, this.pos[1] + sizeY);

        if (KEYBOARD_MANAGER.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_LEFT)
                && overlaying) {
            clicked();
        }

        if (this.buttonOptionType != null) {
            if (!overlaying) {
                context.blit(this.texture, this.pos[0] - sizeX, this.pos[1] - sizeY, 0, 0, 20, 20, 20, 20);
            } else {
                context.blit(this.overlayTexture, this.pos[0] - sizeX, this.pos[1] - sizeY, 0, 0, 20, 20, 20, 20);
            }
        }

        // Text
        context.drawString(HideawayPlus.client().font, title.getString(), this.pos[0] + sizeX + 10, this.pos[1] - sizeY / 2, 0xFFFFFF);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    private void clicked() {
        this.action.run();
        this.buttonWasClicked = true;
        this.playDownSound(HideawayPlus.client().getSoundManager());
    }

    public boolean buttonWasClicked() {
        if (this.buttonWasClicked) {
            this.buttonWasClicked = false;
            return true;
        }
        return false;
    }
}