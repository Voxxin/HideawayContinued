package continued.hideaway.mod.feat.rendering.screen;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.HideawayPlusConfig;
import continued.hideaway.mod.feat.rendering.screen.util.ButtonScreenWidget;
import continued.hideaway.mod.feat.rendering.screen.util.OutfitItemsWidget;
import continued.hideaway.mod.feat.rendering.screen.util.RotationSliderWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WardrobeOutfitScreen extends Screen {
    private int oldScreenHeight = HideawayPlus.client().screen.height;
    private int oldScreenWidth = HideawayPlus.client().screen.width;
    protected WardrobeOutfitScreen() {
        super(Component.nullToEmpty(""));
    }

    @Override
    public void init() {
        super.init();
        assert HideawayPlus.client().screen != null;
        HideawayPlusConfig.init();

        this.addRenderableWidget(new OutfitItemsWidget(oldScreenWidth, oldScreenHeight, Component.translatable("widget.model_slider.title")));
    }


    @Override
    public void tick() {
        super.tick();
        if (oldScreenHeight != HideawayPlus.client().screen.height || oldScreenWidth != HideawayPlus.client().screen.width) {
            oldScreenHeight = HideawayPlus.client().screen.height;
            oldScreenWidth = HideawayPlus.client().screen.width;
            this.rebuildWidgets();

        }
    }

    @Override
    public void onClose() {
        super.onClose();
        HideawayPlus.client().setScreen(new WardrobeScreen());
    }
}
