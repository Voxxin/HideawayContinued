package continued.hideaway.mod.feat.rendering.screen.util;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.keyboard.KeyboardEventInstance;
import continued.hideaway.mod.feat.keyboard.KeyboardManager;
import continued.hideaway.mod.feat.wardrobe.Wardrobe;
import continued.hideaway.mod.util.GuiUtils;
import continued.hideaway.mod.util.WidgetUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;

public class OutfitItemsWidget extends AbstractWidget {
    private static String selectedItemCategory = "head";
    private static int rowsOfSlots = 10;

    private static int[] startingPosItems = new int[]{16, 32};

    private static int screenWidth = HideawayPlus.client().screen.width;
    private int screenHeight = HideawayPlus.client().screen.height;

    private static int scrollRow = 0;
    private ArrayList<Slot> slots = new ArrayList<>();

    private ArrayList<Category> categories = new ArrayList<>();

    private ArrayList<Button> buttons = new ArrayList<>();
    private NameBar nameBar;

    private boolean nameBarSelected = false;
    private int nameBarBlob = 0;

    private final KeyboardEventInstance keyboardEvents = new KeyboardEventInstance();


    public OutfitItemsWidget(int width, int height, Component message) {
        super(0, 0, HideawayPlus.client().screen.width, HideawayPlus.client().screen.height, message);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        setupSlots(guiGraphics);
        setupCategories(guiGraphics);
        setupItems(guiGraphics);
        setupOutfitNameAndSave(guiGraphics);

        switch (selectedItemCategory) {
            case "head" -> {
                for (ItemStack itemStack : Wardrobe.headWear) {
                }
                break;
            }
            case "chest" -> {
                for (ItemStack itemStack : Wardrobe.chestWear) {
                }
            }
            case "holdable" -> {
                for (ItemStack itemStack : Wardrobe.holdable) {
                }
            }
        }

        if (slots.stream().anyMatch(slot -> slot.isOverlaying(mouseX, mouseY)) || categories.stream().anyMatch(category -> category.isOverlaying(mouseX, mouseY)))
            isHovered = true;
        if (slots.stream().noneMatch(slot -> slot.isOverlaying(mouseX, mouseY)) && categories.stream().noneMatch(category -> category.isOverlaying(mouseX, mouseY)))
            isHovered = true;


        if (isHovered && !KeyboardManager.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Slot slot = slots.stream().filter(thisSlot -> thisSlot.isOverlaying((int) mouseX, (int) mouseY)).findFirst().orElseGet(() -> null);
            Category category = categories.stream().filter(thisCategory -> thisCategory.isOverlaying((int) mouseX, (int) mouseY)).findFirst().orElseGet(() -> null);

            if (slot != null) {
                int[] pos = slot.getPos();
                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            }

            if (category != null) {
                int[] pos = category.getPos();
                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            }
        }

        if (isHovered && KeyboardManager.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_1)) {
            this.playDownSound(Minecraft.getInstance().getSoundManager());

            Slot slot = slots.stream().filter(thisSlot -> thisSlot.isOverlaying((int) mouseX, (int) mouseY)).findFirst().orElseGet(() -> null);
            Category category = categories.stream().filter(thisCategory -> thisCategory.isOverlaying((int) mouseX, (int) mouseY)).findFirst().orElseGet(() -> null);
            Button button = buttons.stream().filter(thisButton -> thisButton.isOverlaying((int) mouseX, (int) mouseY)).findFirst().orElseGet(() -> null);


            if (slot != null) {
                int indexOfSlot = slots.indexOf(slot);
                int[] pos = slot.getPos();

                if (getItemsList().size() > indexOfSlot) applyItem(getItemsList().get(indexOfSlot + (scrollRow * 9)));

                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            }

            if (category != null) {
                int[] pos = category.getPos();
                selectedItemCategory = category.type;
                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            }

            if (button != null) {
                int[] pos = button.getPos();
//                if (button.type.equals("save")) {
//                    Wardrobe.applyOutfit();
//                    HideawayPlus.client().setScreen(null);
//                } else if (button.type.equals("delete")) {
//                    Wardrobe.outfit = null;
//                    HideawayPlus.client().setScreen(null);
//                }
                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            }

            if (nameBar != null && nameBar.isOverlaying(mouseX, mouseY)) {
                nameBarSelected = true;
            } else nameBarSelected = false;
        }
    }

    private void setupCategories(GuiGraphics guiGraphics) {
        int sizeOfSlotX = 48;
        int sizeOfSlotY = 16;
        int slotsY = 3;

        categories.clear();

        String[] categoryList = {"head", "chest", "holdable"};

        for (int i = 0; i < categoryList.length; i++) {
            int startX = startingPosItems[0] + (i * sizeOfSlotX);
            int startY = startingPosItems[1] - 16;
            int endX = startX + sizeOfSlotX;
            int endY = startY + sizeOfSlotY;

            if (!categories.contains(new Category(categoryList[i], startX, startY, endX, endY)))
                categories.add(new Category(categoryList[i], startX, startY, endX, endY));

            guiGraphics.fill(startX, startY, endX, endY, 0x80FFFFFF);
        }
    }

    private void setupSlots(GuiGraphics guiGraphics) {
        int sizeOfSlot = 16;
        int slotsY = 9;
        int availableHeight = screenHeight - startingPosItems[1] - 32;
        rowsOfSlots = availableHeight / sizeOfSlot;
        int totalSlots = rowsOfSlots * slotsY;

        slots.clear();

        if (getItemsSize() > totalSlots) {
        } else scrollRow = 0;

        for (int i = 0; i < rowsOfSlots; i++) {
            for (int j = 0; j < slotsY; j++) {
                int startX = startingPosItems[0] + (j * sizeOfSlot);
                int startY = startingPosItems[1] + (i * sizeOfSlot);
                int endX = startX + sizeOfSlot;
                int endY = startY + sizeOfSlot;

                if (!slots.contains(new Slot(startX, startY, endX, endY)))
                    slots.add(new Slot(startX, startY, endX, endY));

                guiGraphics.fill(startX, startY, endX, endY, 0x80FFFFFF);
            }
        }
    }

    private void setupItems(GuiGraphics guiGraphics) {
        int sizeOfSlot = 16;
        int slotsY = 9;
        int availableHeight = screenHeight - startingPosItems[1] - 32;
        rowsOfSlots = availableHeight / sizeOfSlot;
        int totalSlots = rowsOfSlots * slotsY;

        int itemIterator = -1;

        for (int i = 0; i < rowsOfSlots; i++) {
            for (int j = 0; j < slotsY; j++) {
                itemIterator++;

                int startX = startingPosItems[0] + (j * sizeOfSlot);
                int startY = startingPosItems[1] + (i * sizeOfSlot);
                int endX = startX + sizeOfSlot;
                int endY = startY + sizeOfSlot;

                assert getItemsList() != null;
                if (itemIterator > getItemsList().size() - 1) break;
                guiGraphics.renderItem(getItemsList().get(itemIterator), startX, startY, 42);
            }
            if (itemIterator >= getItemsList().size() - 1) break;
        }
    }

    private void setupOutfitNameAndSave(GuiGraphics guiGraphics) {
        int sizeOfNameY = 128;
        int sizeOfButtons = 16;
        int sizingY = 16;

        int spacing = 16;

        int xPos = screenHeight - startingPosItems[1] - 32;

        int startPosName = screenWidth / 2 - sizeOfNameY / 2;

        int saveBtn = startPosName + sizeOfNameY + spacing;
        int deleteBtn = (screenWidth / 2 + sizeOfNameY / 2) - sizeOfNameY - spacing;

        // Outfit name
        guiGraphics.fill(startPosName, xPos, startPosName + sizeOfNameY, xPos + sizingY, 0x80FFFFFF);
        if (nameBar == null)
            nameBar = new NameBar("Outfit Name", startPosName, xPos, startPosName + sizeOfNameY, xPos + sizingY);

        if (nameBar != null) {
            int tickTotal = 60;

            nameBar.text = keyboardEvents.update(nameBar.text);

            String name = nameBar.text;
            if (nameBarSelected && nameBarBlob > tickTotal / 2) name = name + "|";

            guiGraphics.drawString(HideawayPlus.client().font, name, nameBar.aX + 2, nameBar.aY + (sizingY / 4), Color.WHITE.getRGB(), true);
            nameBarBlob++;
            if (nameBarBlob >= tickTotal) nameBarBlob = 0;
        }

        // Save button
        guiGraphics.fill(saveBtn, xPos, saveBtn + sizeOfButtons, xPos + sizingY, 0x80FFFFFF);

        // Cancel button
        guiGraphics.fill(deleteBtn, xPos, deleteBtn - sizeOfButtons, xPos + sizingY, 0x80FFFFFF);


    }

    private static int getItemsSize() {
        switch (selectedItemCategory) {
            case "head" -> {
                return Wardrobe.headWear.size();
            }
            case "chest" -> {
                return Wardrobe.chestWear.size();
            }
            case "holdable" -> {
                return Wardrobe.holdable.size();
            }
        }
        return 0;
    }

    private static ArrayList<ItemStack> getItemsList() {
        switch (selectedItemCategory) {
            case "head" -> {
                return Wardrobe.headWear;
            }
            case "chest" -> {
                return Wardrobe.chestWear;
            }
            case "holdable" -> {
                return Wardrobe.holdable;
            }
        }
        return null;
    }

    private static void applyItem(ItemStack item) {
        switch (selectedItemCategory) {
            case "head" -> {
                Wardrobe.wardrobePlayer.setItemSlot(EquipmentSlot.HEAD, item);
            }
            case "chest" -> {
                Wardrobe.wardrobePlayer.setItemSlot(EquipmentSlot.CHEST, item);
            }
            case "holdable" -> {
                Wardrobe.wardrobePlayer.setItemSlot(EquipmentSlot.OFFHAND, item);
            }
        }
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    private static class Slot {
        int aX;
        int aY;
        int bX;
        int bY;

        public Slot(int aX, int aY, int bX, int bY) {
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

    private static class Category {
        String type;
        int aX;
        int aY;
        int bX;
        int bY;

        public Category(String type, int aX, int aY, int bX, int bY) {
            this.type = type;
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

    private static class Button {
        String type;
        int aX;
        int aY;
        int bX;
        int bY;

        public Button(String type, int aX, int aY, int bX, int bY) {
            this.type = type;
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

    private static class NameBar {
        String text;
        int aX;
        int aY;
        int bX;
        int bY;

        public NameBar(String text, int aX, int aY, int bX, int bY) {
            this.text = text;
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
}