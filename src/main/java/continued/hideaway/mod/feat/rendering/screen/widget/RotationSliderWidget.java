package continued.hideaway.mod.feat.rendering.screen.widget;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.ext.EntityAccessor;
import continued.hideaway.mod.feat.keyboard.KeyboardManager;
import continued.hideaway.mod.feat.wardrobe.Wardrobe;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class RotationSliderWidget extends AbstractWidget {

    int tutDisplay = -1;
    boolean btnRightReleased = true;
    private int startX = 0;
    private int startY = 0;
    private int width = 0;
    private int height = 0;

    private static final ResourceLocation SLIDER_TEXTURE = new ResourceLocation("hideaway_plus", "textures/slider.png");

    private int[] buttonPoints = new int[4];
    private int[] rotPoints = new int[3];
    public RotationSliderWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);

        this.width = width;
        this.height = height;

        this.startX = x;
        this.startY = y;

        rotPoints[0] = width%25;
        rotPoints[1] = width%50;
        rotPoints[2] = width%75;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.button.model_slider"));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (Wardrobe.wardrobePlayer == null) return;

        int minX = this.startX - this.width / 2;
        int maxX = this.startX + this.width / 2;
        int minY = this.startY;
        int maxY = this.startY - this.height;
        int innerOffset = 5;

        buttonPoints[0] = minX + innerOffset;
        buttonPoints[1] = maxX - innerOffset;
        buttonPoints[2] = minY - innerOffset;
        buttonPoints[3] = maxY + innerOffset;

        if (KeyboardManager.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_LEFT))
            clicked(mouseX, mouseY);
        if (KeyboardManager.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_RIGHT) && btnRightReleased)
            continuePlayerAnim(mouseX, mouseY);
        else if (KeyboardManager.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_RIGHT))
            btnRightReleased = true;

        if (tutDisplay > 0) {
            String tut = Component.translatable("widget.model_slider.tutorial").getString();
            guiGraphics.drawCenteredString(HideawayPlus.client().font, tut,  HideawayPlus.client().screen.width/2, maxY - 10, Color.WHITE.getRGB());
            tutDisplay--;
        } else if (tutDisplay == 0) tutDisplay = -2;

        int percentDone = (int) (Wardrobe.wardrobePlayer.yBodyRot % 360);
        float percentage = percentDone / 360.0f;
        if (percentage <= 0f) percentage = 1.0f + percentage;
        int currentPosition = (int) (buttonPoints[0] + (buttonPoints[1] - buttonPoints[0]) * percentage);

        guiGraphics.blit(SLIDER_TEXTURE, minX, minY - height, 0, 0, width, height, 175, 30);
        guiGraphics.blit(SLIDER_TEXTURE, buttonPoints[0], buttonPoints[2] - 10, 0, 20, (int) (160 * percentage), 10, 175, 30);
        guiGraphics.blit(SLIDER_TEXTURE, currentPosition - 3, minY - height, 170, 0, 5, 20, 175, 30);
    }

    @Override
    public boolean clicked(double mouseX, double mouseY) {
        if (!((mouseX >= buttonPoints[0] && mouseX <= buttonPoints[1]) && (mouseY >= buttonPoints[3] && mouseY <= buttonPoints[2]))) return false;
        if (Wardrobe.wardrobePlayer == null) return false;

        if (tutDisplay == -1) tutDisplay = 100;

        int percentClickedAt = (int) ((mouseX - buttonPoints[0]) / (buttonPoints[1] - buttonPoints[0]) * 360);
        ((EntityAccessor)Wardrobe.wardrobePlayer).hp$setRot(percentClickedAt);
        StaticValues.playerRotationSet = true;
        return true;
    }

    private void continuePlayerAnim(int mouseX, int mouseY) {
        if (!((mouseX >= buttonPoints[0] && mouseX <= buttonPoints[1]) && (mouseY >= buttonPoints[3] && mouseY <= buttonPoints[2]))) return;
        if (Wardrobe.wardrobePlayer == null) return;

        btnRightReleased = false;

        this.playDownSound(Minecraft.getInstance().getSoundManager());
        StaticValues.playerRotationSet = false;
    }
}
