package continued.hideaway.mod.feat.rendering.screen.widget;

import continued.hideaway.mod.feat.keyboard.KeyboardManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SkinsBarWidget extends AbstractWidget {
    private Consumer<String> responder;

    private double scroll = 0;
    private int width = 0;
    private int height = 0;

    private ArrayList<Intractable> intractables = new ArrayList<>();
    private List<Slots> slotsList = new ArrayList<>();

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("narration.button.model_slider"));
    }

    public SkinsBarWidget(int width, int height, Component message) {
        super(0, 0, width, height, message);
        this.height = height;
        this.width = width;
    }

    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (scroll != 0) {
            System.out.println(scroll);
            scroll = 0;
        }

        setupArrayNums();
        setupInteractiveAndSkinSlots(guiGraphics);

        if (!KeyboardManager.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_1)) {
            for (Intractable intractable : intractables) {
                if (intractable.isOverlaying(mouseX, mouseY)) {
                    guiGraphics.fill(intractable.aX, intractable.aY, intractable.bX, intractable.bY, 0x80FFFFFF);
                }
            }
        } else {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
        }
    }

    private void setupArrayNums() {
        int center = this.width / 2;
        int sizeOfSlots = 40 + (this.width / 40);
        int yCoordinate = this.height - sizeOfSlots;

        int availableWidth = this.width - sizeOfSlots; // Subtract one slot's width
        int numSlots = availableWidth / sizeOfSlots;

        if (numSlots <= 0) {
            return;
        }

        int totalWidth = numSlots * sizeOfSlots;
        int startX = center - (totalWidth / 2);

        slotsList.clear();
        slotsList = new ArrayList<>();

        for (int i = 0; i < numSlots; i++) {
            int aX = startX + (i * sizeOfSlots);
            int bX = aX + sizeOfSlots;
            int bY = yCoordinate + sizeOfSlots;

            Slots slot = new Slots("", sizeOfSlots, aX, yCoordinate, bX, bY);

            if (slotsList.contains(slot)) continue;
            slotsList.add(slot);
        }

        if (slotsList.size() > 3) {
            Slots arrowLeft = slotsList.get(0);
            Slots addSkinBtn = slotsList.get(slotsList.size() - 2);
            Slots arrowRight = slotsList.get(slotsList.size() - 1);
            arrowRight.slotType = "btnArrowLeft";
            addSkinBtn.slotType = "btnAddSkin";
            arrowLeft.slotType = "btnArrowRight";
        }
    }

    private void setupInteractiveAndSkinSlots(GuiGraphics guiGraphics) {
        intractables.clear();
        for (Slots slot : slotsList) {
            if (slot.slotType.equalsIgnoreCase("btn")) {
                Intractable intractable = new Intractable(slot.slotType, slot.aX, slot.aY, slot.bX, slot.bY);
               intractables.add(intractable);
            } else {
                slot.slotType = "skin";
                Intractable intractable = new Intractable("skin", slot.aX, slot.aY, slot.bX, slot.bY);
                intractables.add(intractable);
                int[] pos = intractable.getPos();
                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            }
        }

    }


    public void setResponder(Consumer<String> responder) {
        this.responder = responder;
    }

    public void setValue(String value) {
        try {
            this.scroll = Double.parseDouble(value);
            if (this.scroll != 0) return;
        } catch (NumberFormatException ignored) {}
    }

    private static class Intractable {
        String name;
        int aX;
        int aY;
        int bX;
        int bY;

        public Intractable(String name, int aX, int aY, int bX, int bY) {
            this.name = name;
            this.aX = aX;
            this.aY = aY;
            this.bX = bX;
            this.bY = bY;
        }

        public int[] getPos() {
            return new int[]{aX, aY, bX, bY};
        }

        public boolean isOverlaying(int mouseX, int mouseY) {
            return mouseX >= aX && mouseX <= bX && mouseY >= aY && mouseY <= bY;
        }
    }

    private static class Slots {
        String slotType;
        int sizeOfSlots;
        int aX;
        int aY;
        int bX;
        int bY;

        public Slots(String slotType, int sizeOfSlots, int aX, int aY, int bX, int bY) {
            this.slotType = slotType;
            this.sizeOfSlots = sizeOfSlots;
            this.aX = aX;
            this.aY = aY;
            this.bX = bX;
            this.bY = bY;
        }

        public int[] getPos() {
            return new int[]{aX, aY, bX, bY};
        }

        public boolean isOverlaying(int mouseX, int mouseY) {
            return mouseX >= aX && mouseX <= bX && mouseY >= aY && mouseY <= bY;
        }
    }

    private static class Skin {
        String skinURL;

        public Skin() {
        }
    }

}
