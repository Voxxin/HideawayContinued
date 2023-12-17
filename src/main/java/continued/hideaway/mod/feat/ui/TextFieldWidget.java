package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.HideawayPlus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class TextFieldWidget extends AbstractWidget {

    private int colour;
    private Component text;
    private final int[] pos;
    private final int[] size;

    public TextFieldWidget(int x, int y, int width, int height, Component message, int colour) {
        super(x, y, width, height, message);
        this.colour = colour;
        this.text = message;
        this.pos = new int[]{x, y};
        this.size = new int[]{width, height};
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        context.drawCenteredString(HideawayPlus.client().font, text, this.pos[0] + this.size[0]/2, this.pos[1], this.colour);
    }
}