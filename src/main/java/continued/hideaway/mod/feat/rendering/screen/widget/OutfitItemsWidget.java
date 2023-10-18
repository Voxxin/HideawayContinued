package continued.hideaway.mod.feat.rendering.screen.widget;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.HideawayPlusConfig;
import continued.hideaway.mod.feat.keyboard.KeyboardManager;
import continued.hideaway.mod.feat.wardrobe.WardrobeUtil;
import continued.hideaway.mod.feat.wardrobe.Wardrobe;
import continued.hideaway.mod.feat.wardrobe.WardrobeOutfit;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class OutfitItemsWidget extends AbstractWidget {
    private static String selectedItemCategory = "head";
    private static int screenWidth;
    private final int screenHeight;
    private static final int[] rowsOfSlots = new int[]{0, 10};

    private static final int[] sizeofSlots = new int[]{0, 0};

    private static final int[] startingPosItems = new int[]{16, 32};

    private static final int[] rowsOfSlotsO = new int[]{0, 10};

    private static final int[] sizeofSlotsO = new int[]{0, 0};

    private static final int[] startingPosItemsO = new int[]{16, 32};

    private static int scrollRow = 0;
    private static int scrollRowO = 0;
    private final ArrayList<Slot> slots = new ArrayList<>();

    private final ArrayList<Category> categories = new ArrayList<>();

    private final ArrayList<Button> buttons = new ArrayList<>();

    private final ArrayList<OutfitSlot> outfits = new ArrayList<>();

    private ArrayList<NameBar> nameBars = new ArrayList<>();

    private boolean nameBarSelected = false;
    private int nameBarBlob = 0;

    private WardrobeOutfit oldOutfit = null;

    private final String[] categoryList = {"head", "chest", "holdable"};

    @Nullable
    private Consumer<String> responder;

    public String newString;
    public double scroll;
    private NameBar oldNameBar;

    public OutfitItemsWidget(int width, int height, Component message) {
        super(0, 0, width, height, message);
        this.screenHeight = height;
        screenWidth = width;
        scrollRow = 0;
        scrollRowO = 0;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable("narration.button.model_slider"));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        setupSlots(guiGraphics);
        setupCategories(guiGraphics);
        setupItems(guiGraphics);
        setupOutfitNameAndButtons(guiGraphics);

        setupOutfitSlots(guiGraphics);
        setupOutfitItems(guiGraphics);
        setupOutfitsNameHover(guiGraphics);

        for (NameBar nameBar : nameBars) {
            if (nameBar.isFocused()) {
                nameBar.text = KeyboardManager.parseKeyInput(newString, nameBar.text);
                newString = "";
            }
        }

        if (slots.stream().anyMatch(slot -> slot.isOverlaying(mouseX, mouseY)) || categories.stream().anyMatch(category -> category.isOverlaying(mouseX, mouseY)))
            isHovered = true;
        if (slots.stream().noneMatch(slot -> slot.isOverlaying(mouseX, mouseY)) && categories.stream().noneMatch(category -> category.isOverlaying(mouseX, mouseY)))
            isHovered = true;


        if (isHovered && !KeyboardManager.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Slot slot = slots.stream().filter(thisSlot -> thisSlot.isOverlaying(mouseX, mouseY)).findFirst().orElseGet(() -> null);
            OutfitSlot outfitSlot = outfits.stream().filter(thisOutfitSlot -> thisOutfitSlot.isOverlaying(mouseX, mouseY)).findFirst().orElseGet(() -> null);

            Category category = categories.stream().filter(thisCategory -> thisCategory.isOverlaying(mouseX, mouseY)).findFirst().orElseGet(() -> null);

            NameBar outfitBar = nameBars.stream().filter(thisNameBar -> thisNameBar.title.equals("nameBarSearch")).findFirst().orElseGet(() -> null);
            if (outfitBar == null) return;

            NameBar newOutfitBar = nameBars.stream().filter(thisNameBar -> thisNameBar.title.equals("outfitName")).findFirst().orElseGet(() -> null);
            if (newOutfitBar == null) return;

            if (slot != null) {
                if (scroll != 0) {
                    int totalSlots = rowsOfSlots[0] * rowsOfSlots[1];
                    if (getItemsSize() > totalSlots) {
                        if (scroll < 0 && (getItemsSize() - totalSlots > scrollRow * rowsOfSlots[0])) {
                            scrollRow += 1;
                        } else if (scroll > 0) scrollRow -= 1;
                        if (scrollRow < 0) scrollRow = 0;
                        scroll = 0;
                    }
                }

                int[] pos = slot.getPos();
                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            }

            if (outfitSlot != null) {
                if (scroll != 0) {
                    int totalSlots = rowsOfSlotsO[0] * rowsOfSlotsO[1];
                    if (StaticValues.wardrobeOutfits.size() > totalSlots) {
                        if (scroll < 0 && (StaticValues.wardrobeOutfits.size() - totalSlots > scrollRowO * rowsOfSlotsO[0])) {
                            scrollRowO += 1;
                        } else if (scroll > 0) scrollRowO -= 1;

                        if (scrollRowO < 0) scrollRowO = 0;
                        scroll = 0;
                    }
                }

                int indexOfSlot = outfits.indexOf(outfitSlot);
                indexOfSlot += (scrollRowO * rowsOfSlotsO[0]);
                if (StaticValues.wardrobeOutfits.size() > indexOfSlot) outfitBar.text = StaticValues.wardrobeOutfits.get(indexOfSlot).title;
                else outfitBar.text = "";

                int[] pos = outfitSlot.getPos();
                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            } else outfitBar.text = "";

            if (category != null) {
                int[] pos = category.getPos();
                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            }
        }

        if (isHovered && KeyboardManager.isMouseKey(GLFW.GLFW_MOUSE_BUTTON_1)) {
            Slot slot = slots.stream().filter(thisSlot -> thisSlot.isOverlaying(mouseX, mouseY)).findFirst().orElseGet(() -> null);
            OutfitSlot outfitSlot = outfits.stream().filter(thisOutfitSlot -> thisOutfitSlot.isOverlaying(mouseX, mouseY)).findFirst().orElseGet(() -> null);

            Category category = categories.stream().filter(thisCategory -> thisCategory.isOverlaying(mouseX, mouseY)).findFirst().orElseGet(() -> null);
            Button button = buttons.stream().filter(thisButton -> thisButton.isOverlaying(mouseX, mouseY)).findFirst().orElseGet(() -> null);

            NameBar nameBar = nameBars.stream().filter(thisNameBar -> thisNameBar.isOverlaying(mouseX, mouseY)).findFirst().orElseGet(() -> null);

            NameBar outfitBar = nameBars.stream().filter(thisNameBar -> thisNameBar.title.equals("nameBarSearch")).findFirst().orElseGet(() -> null);
            if (outfitBar == null) return;

            NameBar newOutfitBar = nameBars.stream().filter(thisNameBar -> thisNameBar.title.equals("outfitName")).findFirst().orElseGet(() -> null);
            if (newOutfitBar == null) return;


            if (slot != null) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());

                int indexOfSlot = slots.indexOf(slot);
                if (indexOfSlot != -1) {
                    int[] pos = slot.getPos();
                    if (getItemsList().size() > indexOfSlot)
                        applyItem(getItemsList().get(indexOfSlot + (scrollRow * rowsOfSlots[0])));

                    guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
                }
            }

            if (outfitSlot != null) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());

                int indexOfSlot = outfits.indexOf(outfitSlot);
                int[] pos = outfitSlot.getPos();

                if (StaticValues.wardrobeOutfits.size() - scrollRowO * rowsOfSlotsO[0] > indexOfSlot) applyOutfit(indexOfSlot);
                if (StaticValues.wardrobeOutfits.size() - scrollRowO * rowsOfSlotsO[0]> indexOfSlot) outfitBar.text = StaticValues.wardrobeOutfits.get(indexOfSlot).title;
                else outfitBar.text = "";

                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            } else outfitBar.text = "";

            if (category != null) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());

                int[] pos = category.getPos();
                selectedItemCategory = category.type;
                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            }

            if (button != null) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());

                int[] pos = button.getPos();
                if (button.type.equals("save")) {
                    WardrobeOutfit copyOfOutfit = WardrobeUtil.getOutfit();

                    if (WardrobeUtil.validOutfit(newOutfitBar.text)) {
                        StaticValues.newOutfit =
                                new WardrobeOutfit(newOutfitBar.text,
                                        copyOfOutfit != null ? copyOfOutfit.fileName : "",
                                        "",
                                        Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.HEAD),
                                        Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.CHEST),
                                        Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.OFFHAND));
                        HideawayPlusConfig.updateOutfits();
                        scrollRowO = 0;
                    }
                } else if (button.type.equals("delete")) {
                    WardrobeOutfit copyOfOutfit = WardrobeUtil.getOutfit();
                    StaticValues.wardrobeOutfits.remove(WardrobeUtil.getOutfit());
                    StaticValues.newOutfit =
                            new WardrobeOutfit(copyOfOutfit.title,
                                    copyOfOutfit.fileName,
                                    "delete",
                                    copyOfOutfit.head,
                                    copyOfOutfit.chest,
                                    copyOfOutfit.holdable);

                    scrollRowO = 0;
                    HideawayPlusConfig.updateOutfits();
                } else if (button.type.equals("clear")) {
                    WardrobeUtil.clearOutfit();
                }
                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            }

            if (nameBar != null && !nameBar.focused) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());

                int[] pos = nameBar.getPos();
                nameBar.setFocused(true);
                guiGraphics.fill(pos[0], pos[1], pos[2], pos[3], 0x80C0C0C0);
            } else if (nameBar == null) {
                for (NameBar namebars : nameBars) {
                    if (namebars.isFocused() && !namebars.isOverlaying(mouseX, mouseY)) {
                        namebars.setFocused(false);
                    }
                }
            }
        }
    }

    private void setupCategories(GuiGraphics guiGraphics) {
        int sizeOfSlotX = sizeofSlots[0]/categoryList.length;
        int sizeOfSlotY = 16;

        categories.clear();

        for (int i = 0; i < categoryList.length; i++) {
            int startX = startingPosItems[0] + (i * sizeOfSlotX);
            int startY = startingPosItems[1] - sizeOfSlotY;
            int endX = startX + sizeOfSlotX;
            int endY = startY + sizeOfSlotY;

            if (!categories.contains(new Category(categoryList[i], startX, startY, endX, endY)))
                categories.add(new Category(categoryList[i], startX, startY, endX, endY));

            guiGraphics.fill(startX, startY, endX, endY, 0x80FFFFFF);
        }
    }


    private void setupSlots(GuiGraphics guiGraphics) {
        int sizeOfSlot = 16;

        int originalWidth = (screenWidth / sizeOfSlot) / 6;
        rowsOfSlots[0] = originalWidth + (categoryList.length - (originalWidth % categoryList.length));

        int availableHeight = screenHeight - startingPosItems[1] - 46;
        rowsOfSlots[1] = availableHeight / sizeOfSlot;
        int totalSlots = rowsOfSlots[1] * rowsOfSlots[0];

        sizeofSlots[0] = rowsOfSlots[0] * sizeOfSlot;
        sizeofSlots[1] = rowsOfSlots[1] * sizeOfSlot;

        slots.clear();

        for (int i = 0; i < rowsOfSlots[1]; i++) {
            for (int j = 0; j < rowsOfSlots[0]; j++) {
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

    private void setupOutfitSlots(GuiGraphics guiGraphics) {
        int sizeOfSlot = 32;
        int edgeMargin = 16;

        int originalWidth = (screenWidth / sizeOfSlot) / 6;
        rowsOfSlotsO[0] = originalWidth + (categoryList.length - (originalWidth % categoryList.length));

        startingPosItemsO[0] = edgeMargin;

        int availableHeight = screenHeight - startingPosItemsO[1] - 32;
        rowsOfSlotsO[1] = availableHeight / sizeOfSlot;

        sizeofSlotsO[0] = rowsOfSlotsO[0] * sizeOfSlot;
        sizeofSlotsO[1] = rowsOfSlotsO[1] * sizeOfSlot;

        outfits.clear();

        int startXOpposite = screenWidth - sizeofSlotsO[0] - edgeMargin;
        int startYOpposite = startingPosItemsO[1];

        for (int i = 0; i < rowsOfSlotsO[1]; i++) {
            for (int j = 0; j < rowsOfSlotsO[0]; j++) {
                int startX = startXOpposite + (j * sizeOfSlot);
                int startY = startYOpposite + (i * sizeOfSlot);
                int endX = startX + sizeOfSlot;
                int endY = startY + sizeOfSlot;

                if (!outfits.contains(new Slot(startX, startY, endX, endY))) {
                    outfits.add(new OutfitSlot(startX, startY, endX, endY));
                    guiGraphics.fill(startX, startY, endX, endY, 0x80FFFFFF);
                }
            }
        }
    }


    private void setupItems(GuiGraphics guiGraphics) {
        int sizeOfSlot = 16;
        int startIndex = scrollRow * rowsOfSlots[0];
        int totalSlots = rowsOfSlots[1] * rowsOfSlots[0];
        int endIndex = Math.min(startIndex + getItemsList().size(), totalSlots);

        for (int i = startIndex; i < endIndex; i++) {
            int itemIterator = i - startIndex;

            int j = itemIterator % rowsOfSlots[0];
            int iRow = itemIterator / rowsOfSlots[0];

            int startX = startingPosItems[0] + (j * sizeOfSlot);
            int startY = startingPosItems[1] + (iRow * sizeOfSlot);

            assert getItemsList() != null;
            if (i >= getItemsList().size()) break;
            guiGraphics.renderItem(getItemsList().get(i), startX, startY, 42);
        }
    }


    private void setupOutfitItems(GuiGraphics guiGraphics) {
        int sizeOfSlot = 16;
        int startIndex = scrollRowO * rowsOfSlotsO[0];
        int endIndex = Math.min(startIndex + outfits.size(), StaticValues.wardrobeOutfits.size());

        for (int i = startIndex; i < endIndex; i++) {
            OutfitSlot outfitSlot = outfits.get(i - startIndex);
            WardrobeOutfit outfit = StaticValues.wardrobeOutfits.get(i);

            if (outfit == null) continue;

            int startX = outfitSlot.aX;
            int startY = outfitSlot.aY;

            for (int j = 0; j < 3; j++) {
                int xOffset = (j == 1) ? sizeOfSlot : 0;
                int yOffset = (j == 2) ? sizeOfSlot : 0;

                ItemStack itemStack = switch (j) {
                    case 0 -> outfit.head;
                    case 1 -> outfit.chest;
                    case 2 -> outfit.holdable;
                    default -> null;
                };

                if (itemStack == null) continue;

                guiGraphics.renderItem(itemStack, startX + xOffset, startY + yOffset, 42);

                if (!WardrobeUtil.itemExists(itemStack)) {
                    guiGraphics.fill(startX + xOffset, startY + yOffset, startX + xOffset + sizeOfSlot, startY + yOffset + sizeOfSlot, 0x80FF0000);
                }
            }
        }
    }

    private void setupOutfitNameAndButtons(GuiGraphics guiGraphics) {
        NameBar nameBar = nameBars.stream()
                .filter(n -> n.title.equals("outfitName"))
                .findFirst()
                .orElseGet(() -> null);

        WardrobeOutfit thisOutfit = WardrobeUtil.outfitExists() ? WardrobeUtil.getOutfit() : null;

        if (thisOutfit != null) {
            if (oldOutfit == null || thisOutfit != oldOutfit) {
                oldOutfit = thisOutfit;

                if (nameBar != null) {
                    nameBar.text = thisOutfit.title;
                }
            }
        }

        if ((thisOutfit == null && oldOutfit != null)) {
            oldOutfit = null;
            if (nameBar != null) {
                nameBar.text = Component.translatable("widget.outfit_editor.basic_outfit_name").getString();
            }
        }

        int sizeOfNameY = 128;
        int sizeOfButtons = 20;
        int sizingY = 20;
        int spacing = 16;
        int xPos = screenHeight - startingPosItems[1] - 32;
        int startPosName = screenWidth / 2 - sizeOfNameY / 2;
        int saveBtn = startPosName + sizeOfNameY + spacing;
        int deleteBtn = (screenWidth / 2 + sizeOfNameY / 2) - sizeOfNameY - spacing;
        int clearButton = startPosName + sizeOfNameY / 2;

        if (nameBar == null) {
            nameBar = nameBars.stream()
                    .filter(n -> n.title.equals("outfitName"))
                    .findFirst()
                    .orElseGet(() -> {
                        NameBar newBar = new NameBar("outfitName", thisOutfit == null
                                ? Component.translatable("widget.outfit_editor.basic_outfit_name").getString()
                                : thisOutfit.title,
                                startPosName, xPos, startPosName + sizeOfNameY, xPos + sizingY);
                        if (!nameBars.contains(newBar)) {
                            nameBars.add(newBar);
                        }
                        return newBar;
                    });
        }

        int tickTotal = 60;
        String name = nameBar.text;

        if (nameBar.isFocused() && nameBarBlob > tickTotal / 2) {
            name = name + "|";
        }

        guiGraphics.drawString(HideawayPlus.client().font, name, nameBar.aX + 2, nameBar.aY + (sizingY / 4), Color.WHITE.getRGB(), true);
        nameBarBlob++;

        if (nameBarBlob >= tickTotal) {
            nameBarBlob = 0;
        }

        buttons.clear();

        if ((!WardrobeUtil.outfitExists() || (thisOutfit != null && !nameBar.text.equals(thisOutfit.title)) && WardrobeUtil.validOutfit(nameBar.text))) {
            this.renderButton(guiGraphics, saveBtn, xPos, sizeOfButtons);
            Button saveButton = new Button("save", saveBtn, xPos, saveBtn + sizeOfButtons, xPos + sizingY);
            if (!buttons.contains(saveButton)) buttons.add(saveButton);
        }

        if (WardrobeUtil.outfitExists()) {
            this.renderButton(guiGraphics, deleteBtn - sizeOfButtons, xPos, sizeOfButtons);
            Button deleteButton = new Button("delete", deleteBtn - sizeOfButtons, xPos, deleteBtn, xPos + sizingY);
            if (!buttons.contains(deleteButton)) buttons.add(deleteButton);
        }

        if (Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.HEAD).getItem() != Items.AIR ||
                Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.CHEST).getItem() != Items.AIR ||
                Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.OFFHAND).getItem() != Items.AIR) {
            guiGraphics.blitNineSliced(WIDGETS_LOCATION, clearButton - (sizeOfButtons * 2), xPos + (sizeOfButtons * 2), sizeOfButtons, sizeOfButtons,20, 4, 0,0, sizeOfButtons);
            if (!buttons.contains(new Button("clear", clearButton - (sizeOfButtons * 2), xPos + (sizeOfButtons * 2), clearButton + sizeOfButtons, xPos + (sizeOfButtons * 3)))) {
                buttons.add(new Button("clear", clearButton - (sizeOfButtons * 2), xPos + (sizeOfButtons * 2), clearButton + sizeOfButtons, xPos + (sizeOfButtons * 3)));
            }
        }
    }


    private void setupOutfitsNameHover(GuiGraphics guiGraphics) {
        int sizeOfNameX = sizeofSlotsO[0];
        int sizeOfNameY = 16;
        int startXOpposite = screenWidth - sizeofSlotsO[0] - startingPosItemsO[0];
        int startYOpposite = startingPosItemsO[1];
        int endX = startXOpposite + sizeOfNameX;
        int endY = startYOpposite - sizeOfNameY;

        NameBar nameBar = nameBars.stream()
                .filter(n -> n.title.equals("nameBarSearch"))
                .findFirst()
                .orElseGet(() -> {
                    NameBar newBar = new NameBar("nameBarSearch", "", startXOpposite, startYOpposite, endX, endY);
                    nameBars.add(newBar);
                    return newBar;
                });

        guiGraphics.fill(nameBar.aX, nameBar.aY, nameBar.bX, nameBar.bY, 0x80FFFFFF);
        guiGraphics.drawString(HideawayPlus.client().font, Component.literal("").append(nameBar.text).withStyle(ChatFormatting.ITALIC), nameBar.aX + 2, nameBar.aY - sizeOfNameY + 2, Color.WHITE.getRGB(), true);
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

    private static void applyOutfit(int index) {
        WardrobeOutfit outfit = StaticValues.wardrobeOutfits.get(index);
        Wardrobe.wardrobePlayer.setItemSlot(EquipmentSlot.HEAD, outfit.head);
        Wardrobe.wardrobePlayer.setItemSlot(EquipmentSlot.CHEST, outfit.chest);
        Wardrobe.wardrobePlayer.setItemSlot(EquipmentSlot.OFFHAND, outfit.holdable);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public void setResponder(Consumer<String> responder) {
        this.responder = responder;
    }

    public void setValue(String value) {
        try {
            this.scroll = Double.parseDouble(value);
            if (this.scroll != 0) return;
        } catch (NumberFormatException ignored) {}

        this.newString = value;
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
        String title;
        String text;
        int aX;
        int aY;
        int bX;
        int bY;
        boolean focused = false;

        public NameBar(String title, String text, int aX, int aY, int bX, int bY) {
            this.title = title;
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

        public boolean isFocused() {
            return this.focused;
        }

        public void setFocused(boolean f) {
            this.focused = f;
        }
    }

    private static class OutfitSlot {
        int aX;
        int aY;
        int bX;
        int bY;

        public OutfitSlot(int aX, int aY, int bX, int bY) {
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

    private void renderButton(GuiGraphics guiGraphics, int x, int y, int width) {
        guiGraphics.blit(WIDGETS_LOCATION, x, y, 0, 66, 2, 20);
        guiGraphics.blit(WIDGETS_LOCATION, x + 2, y, 2, 66, width - 4, 20);
        guiGraphics.blit(WIDGETS_LOCATION, x + width - 2, y, 198, 66, 2, 20);
    }
}