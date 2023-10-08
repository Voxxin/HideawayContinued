package continued.hideaway.mod.feat.rendering.screen;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.model.WardrobeConfigModel;
import continued.hideaway.mod.feat.rendering.screen.widget.ButtonOutfitOptionWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WardrobeConfigScreen extends Screen {
    protected WardrobeConfigScreen() {
        super(Component.nullToEmpty(""));
    }

    @Override
    protected void init() {
        super.init();
        int spacing = 0;
        for (WardrobeConfigModel configModel : WardrobeConfigModel.values()) {
            assert HideawayPlus.client().screen != null;

            int startingPosX = 20;
            int startingPosY = ((HideawayPlus.client().screen.height) / 5) + (30 * spacing);

            this.addRenderableWidget(new ButtonOutfitOptionWidget(startingPosX, startingPosY, 20, 20, configModel));
            spacing++;
        }

    }

    @Override
    public void onClose() {
        super.onClose();
        HideawayPlus.client().setScreen(new WardrobeScreen());
    }
}
