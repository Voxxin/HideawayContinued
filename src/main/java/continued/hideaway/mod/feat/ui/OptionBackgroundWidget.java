package continued.hideaway.mod.feat.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class OptionBackgroundWidget extends AbstractWidget {
    private final int[] pos;
    private final int[] size;

    public OptionBackgroundWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.pos = new int[]{x, y};
        this.size = new int[]{width, height};
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float partialTick) {
        context.fill(this.pos[0], this.pos[1], this.pos[0] + this.size[0], this.pos[1] + this.size[1], 0x80000000);
    }
}