package continued.hideaway.mod.feat.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class OutlineAreaWidget extends AbstractWidget {
    private final int[] pos;
    private final int[] size;
    private final int colour;

    public OutlineAreaWidget(int x, int y, int width, int height, int colour) {
        super(x, y, width, height, Component.empty());
        this.pos = new int[]{x, y};
        this.size = new int[]{width, height};
        this.colour = colour;
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        context.hLine(this.pos[0], this.pos[0] + this.size[0]-1, this.pos[1], this.colour);
        context.hLine(this.pos[0], this.pos[0] + this.size[0]-1, this.pos[1] + this.size[1], this.colour);
        context.vLine(this.pos[0], this.pos[1] + this.size[1], this.pos[1], this.colour);
        context.vLine(this.pos[0] + this.size[0]-1, this.pos[1] + this.size[1], this.pos[1], this.colour);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}