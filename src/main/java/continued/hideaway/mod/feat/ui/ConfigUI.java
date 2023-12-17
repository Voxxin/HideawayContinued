package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.HideawayPlusConfig;
import continued.hideaway.mod.feat.config.model.ButtonCategoryOptions;
import continued.hideaway.mod.feat.config.model.GeneralConfigModel;
import continued.hideaway.mod.feat.config.model.SoundConfigModel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.ArrayList;

public class ConfigUI extends Screen {
    private final Screen parent;
    private double scrollAmount = 0;
    private double screenSpacing = 0;
    private int minScrollY = 0;
    private int maxScrollY = 0;
    private boolean resetAndClear = false;

    public ConfigUI(Screen parent) {
        super(Component.translatable("config.hp-config.general.title"));
        this.parent = parent;
    }

    private static ButtonCategoryOptions currentCategory = ButtonCategoryOptions.GENERAL;

    private final ArrayList<ButtonOptionWidget> buttonWidgets = new ArrayList<>();

    @Override
    protected void init() {
        int optionSpacing = GeneralConfigModel.values().length * 60 + SoundConfigModel.values().length * 60;

        int padding = 60;
        int buttonSize = 20;

        int spacingY = 0;
        int previousOptionsOpened = 0;

        screenSpacing += scrollAmount;
        scrollAmount = 0;

        maxScrollY = -maxScrollY;

        if (screenSpacing >= minScrollY) screenSpacing = minScrollY;

        // Categories
        int spacingHorizontal0 = this.width / ButtonCategoryOptions.values().length;
        int screenSpacingVertical0 = 20;
        int screenHorz0 = 0;
        int screenHorz1 = spacingHorizontal0;

        handleCategories(spacingHorizontal0, screenSpacingVertical0, screenHorz0, screenHorz1, buttonSize);

        if (currentCategory == ButtonCategoryOptions.GENERAL) {
            handleGeneralSettings(padding, buttonSize, spacingY, optionSpacing, previousOptionsOpened);
        }

        if (currentCategory == ButtonCategoryOptions.SOUNDS) {
            handleSoundSettings(padding, buttonSize, optionSpacing, spacingY, previousOptionsOpened);
        }


        if (resetAndClear) {
            resetAndClear = false;
            this.rebuildWidgets();
        }
    }

    private void handleCategories(int spacingHorizontal0, int screenSpacingVertical0, int screenHorz0, int screenHorz1, int buttonSize) {
        for (ButtonCategoryOptions category : ButtonCategoryOptions.values()) {
            ButtonOptionWidget buttonWidget = new ButtonOptionWidget(screenHorz0 + screenHorz1/2, (int) (screenSpacingVertical0 + screenSpacing), screenHorz1 , buttonSize, Component.literal(""), () -> {
                currentCategory = category;
                resetAndClear = true;
                screenSpacing = 0;
                minScrollY = 0;
                maxScrollY = 0;
            }, null);
            OptionBackgroundWidget backgroundWidget = new OptionBackgroundWidget(screenHorz0, (int) (screenSpacingVertical0 + screenSpacing - (buttonSize/2 + buttonSize/4)), spacingHorizontal0, buttonSize);

            if (!buttonWidgets.contains(buttonWidget)) buttonWidgets.add(buttonWidget);
            this.addRenderableWidget(backgroundWidget);
            this.addRenderableWidget(buttonWidget);
            this.addRenderableWidget(new TextFieldWidget(screenHorz0, (int) (screenSpacingVertical0 + screenSpacing - buttonSize/2), spacingHorizontal0, buttonSize, Component.translatable(category.key), Color.WHITE.getRGB()));
            if (currentCategory == category) this.addRenderableWidget(new OutlineAreaWidget(screenHorz0, (int) (screenSpacingVertical0 + screenSpacing - (buttonSize/2 + buttonSize/4)), spacingHorizontal0, buttonSize, Color.WHITE.getRGB()));

            screenHorz0 = screenHorz1;
            screenHorz1 += screenHorz1;
        }
    }

    private void handleGeneralSettings(int padding, int buttonSize, int optionSpacing, int spacingY, int previousOptionsOpened) {
        for (GeneralConfigModel setting : GeneralConfigModel.values()) {
            assert HideawayPlus.client().screen != null;

            int screenSpacingHorizontal = 20;
            int screenSpacingVertical = (int) (padding + (30 * spacingY) + screenSpacing + (previousOptionsOpened * optionSpacing));
            Object value = null;

            int textBarLengthLocal = 60;

            boolean isBoolean = setting.value.equals("true") || setting.value.equals("false");
            boolean isNumber = !isBoolean && setting.value.matches("-?\\d+(\\.\\d+)?");

            if (isBoolean) value = Boolean.parseBoolean(setting.value);
            if (isNumber) value = Float.valueOf(setting.value);
            if (value == null) return;

            if (isBoolean) {
                ButtonOptionWidget optionWidget = new ButtonOptionWidget(screenSpacingHorizontal, screenSpacingVertical, buttonSize, buttonSize, Component.translatable(setting.name),
                        () -> {
                            for (GeneralConfigModel setting1 : GeneralConfigModel.values()) {
                                if (setting1.name.equals(setting.name)) {
                                    setting1.value = String.valueOf(!Boolean.parseBoolean(setting1.value));
                                }
                            }

                            HideawayPlusConfig.write();

                            resetAndClear = true;
                        }, new ButtonOptionType(ButtonOptionTypeModel.CHECKBOX, Boolean.parseBoolean(setting.value)));


                buttonWidgets.add(optionWidget);
                this.addRenderableWidget(optionWidget);
            }

            if (isNumber) {
                EditBox optionWidget = new EditBox(HideawayPlus.client().font, screenSpacingHorizontal - buttonSize/2, screenSpacingVertical, textBarLengthLocal, buttonSize,
                        Component.translatable(setting.name));
                optionWidget.setSuggestion(setting.value);
                optionWidget.setMaxLength(3);

                optionWidget.setResponder(textChangedForWidget -> {
                    textChangedForWidget = textChangedForWidget.replaceAll("[^0-9.]", "");
                    if (!textChangedForWidget.matches("[0-9.]")) return;
                    for (GeneralConfigModel setting1 : GeneralConfigModel.values()) {
                        if (setting1.name.equals(setting.name)) {
                            setting1.value = textChangedForWidget;
                        }
                    }
                });

                this.addRenderableWidget(new TextFieldWidget(screenSpacingHorizontal + (textBarLengthLocal*2) - buttonSize, screenSpacingVertical + buttonSize/4, textBarLengthLocal, buttonSize,
                        Component.translatable(setting.name), Color.WHITE.getRGB()));
                this.addRenderableWidget(optionWidget);
                this.addWidget(optionWidget);
            }

            spacingY++;
        }
    }

    private void handleSoundSettings(int padding, int buttonSize, int optionSpacing, int spacingY, int previousOptionsOpened) {
        for (SoundConfigModel setting : SoundConfigModel.values()) {
            assert HideawayPlus.client().screen != null;

            int screenSpacingHorizontal = 20;
            int screenSpacingVertical = (int) (padding + (30 * spacingY) + screenSpacing + (previousOptionsOpened * optionSpacing));
            Object value = null;

            int textBarLengthLocal = 60;

            boolean isBoolean = setting.value.equals("true") || setting.value.equals("false");
            boolean isNumber = !isBoolean && setting.value.matches("-?\\d+(\\.\\d+)?");

            if (isBoolean) value = Boolean.parseBoolean(setting.value);
            if (isNumber) value = Float.valueOf(setting.value);
            if (value == null) return;

            if (isBoolean) {
                ButtonOptionWidget optionWidget = new ButtonOptionWidget(screenSpacingHorizontal, screenSpacingVertical, buttonSize, buttonSize, Component.translatable(setting.name),
                        () -> {
                            for (SoundConfigModel setting1 : SoundConfigModel.values()) {
                                if (setting1.name.equals(setting.name)) {
                                    setting1.value = String.valueOf(!Boolean.parseBoolean(setting1.value));
                                }
                            }

                            HideawayPlusConfig.write();

                            resetAndClear = true;
                        }, new ButtonOptionType(ButtonOptionTypeModel.CHECKBOX, Boolean.parseBoolean(setting.value)));


                buttonWidgets.add(optionWidget);
                this.addRenderableWidget(optionWidget);
            }

            if (isNumber) {
                EditBox optionWidget = new EditBox(HideawayPlus.client().font, screenSpacingHorizontal - buttonSize/2, screenSpacingVertical, textBarLengthLocal, buttonSize,
                        Component.translatable(setting.name));
                optionWidget.setSuggestion(setting.value);
                optionWidget.setMaxLength(3);

                optionWidget.setResponder(textChangedForWidget -> {
                    textChangedForWidget = textChangedForWidget.replaceAll("[^0-9.]", "");
                    if (!textChangedForWidget.matches("[0-9.]")) return;
                    for (SoundConfigModel setting1 : SoundConfigModel.values()) {
                        if (setting1.name.equals(setting.name)) {
                            setting1.value = textChangedForWidget;
                        }
                    }
                });

                this.addRenderableWidget(new TextFieldWidget(screenSpacingHorizontal + (textBarLengthLocal*2) - buttonSize, screenSpacingVertical + buttonSize/4, textBarLengthLocal, buttonSize,
                        Component.translatable(setting.name), Color.WHITE.getRGB()));
                this.addRenderableWidget(optionWidget);
                this.addWidget(optionWidget);
            }

            spacingY++;
        }
    }

    @Override
    public void tick() {
        super.tick();

        for (ButtonOptionWidget buttonWidget : buttonWidgets) {
            if (buttonWidget.buttonWasClicked()) {
                resetAndClear = true;
            }
        }

        if (resetAndClear) {
            resetAndClear = false;
            this.rebuildWidgets();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderDirtBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        HideawayPlusConfig.write();
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        double accumulatedDelta = 0.0;
        double smoothingFactor = 10;

        accumulatedDelta += scrollY;
        double smoothedDelta = accumulatedDelta * smoothingFactor;
        accumulatedDelta += smoothedDelta;

        scrollAmount = accumulatedDelta;
        this.rebuildWidgets();
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

}
