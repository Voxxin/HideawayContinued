package continued.hideaway.mod.feat.rendering.screen;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.rendering.screen.util.ButtonScreenWidget;
import continued.hideaway.mod.feat.rendering.screen.util.OutfitItemsWidget;
import continued.hideaway.mod.feat.rendering.screen.util.RotationSliderWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WardrobeOutfitScreen extends Screen {
    protected WardrobeOutfitScreen() {
        super(Component.nullToEmpty(""));
    }

    @Override
    public void init() {
        super.init();
        assert HideawayPlus.client().screen != null;
        int ScreenButtonsSize = 20;

        this.addRenderableWidget(new OutfitItemsWidget(ScreenButtonsSize, ScreenButtonsSize, Component.translatable("widget.model_slider.title")));
    }


    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void onClose() {
        super.onClose();
        HideawayPlus.client().setScreen(new WardrobeScreen());
    }
}
