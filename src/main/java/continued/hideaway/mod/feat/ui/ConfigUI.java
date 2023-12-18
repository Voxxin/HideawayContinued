package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.feat.config.HideawayPlusConfig;
import continued.hideaway.mod.feat.config.model.GeneralConfigModel;
import continued.hideaway.mod.feat.config.model.SoundConfigModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

public class ConfigUI extends Screen {
    private final Screen parent;
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, guiEventListener -> this.removeWidget((GuiEventListener)guiEventListener));
    @Nullable
    private TabNavigationBar tabNavigationBar;

    public ConfigUI(Screen parent) {
        super(Component.translatable("config.hp.general.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width).addTabs(new GeneralTab(), new SoundsTab()).build();
        this.addRenderableWidget(this.tabNavigationBar);
        this.tabNavigationBar.selectTab(0, false);
        this.repositionElements();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderDirtBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        this.tabManager.tickCurrent();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void repositionElements() {
        if (this.tabNavigationBar == null) return;
        this.tabNavigationBar.setWidth(this.width);
        this.tabNavigationBar.arrangeElements();

        int i = this.tabNavigationBar.getRectangle().bottom();
        ScreenRectangle screenRectangle = new ScreenRectangle(0, i, this.width, this.height);
        this.tabManager.setTabArea(screenRectangle);
    }


    @Environment(value= EnvType.CLIENT)
    static class GeneralTab
            extends GridLayoutTab {
        private static final Component TITLE = Component.translatable("config.hp.general.title");
        GeneralTab() {
            super(TITLE);
            this.layout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
            GridLayout.RowHelper rowHelper = this.layout.createRowHelper(2);

            for (GeneralConfigModel configModel : GeneralConfigModel.values()) {
                MutableComponent label = Component.translatable(configModel.name).append(": ");

                rowHelper.addChild(Button.builder(label.copy().append(configModel.value ? "ON" : "OFF"), button -> {
                    configModel.value = !configModel.value;
                    HideawayPlusConfig.write();
                    button.setMessage(label.copy().append(configModel.value ? "ON" : "OFF"));
                }).build());
            }
            this.layout.arrangeElements();
        }
    }

    @Environment(value= EnvType.CLIENT)
    static class SoundsTab
            extends GridLayoutTab {
        private static final Component TITLE = Component.translatable("config.hp.sounds.title");
        SoundsTab() {
            super(TITLE);
            this.layout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
            GridLayout.RowHelper rowHelper = this.layout.createRowHelper(2);

            for (SoundConfigModel configModel : SoundConfigModel.values()) {
                MutableComponent label = Component.translatable(configModel.name).append(": ");

                rowHelper.addChild(Button.builder(label.copy().append(configModel.value ? "ON" : "OFF"), button -> {
                    configModel.value = !configModel.value;
                    HideawayPlusConfig.write();
                    button.setMessage(label.copy().append(configModel.value ? "ON" : "OFF"));
                }).build());
            }
            this.layout.arrangeElements();
        }
    }
}
