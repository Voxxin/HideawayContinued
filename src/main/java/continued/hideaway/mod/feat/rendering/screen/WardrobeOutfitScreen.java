package continued.hideaway.mod.feat.rendering.screen;

import continued.hideaway.mod.HideawayPlus;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class WardrobeOutfitScreen extends Screen {
    protected WardrobeOutfitScreen() {
        super(Component.nullToEmpty(""));
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
