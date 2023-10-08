package continued.hideaway.mod.feat.rendering.screen;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.model.ModConfigModel;
import continued.hideaway.mod.feat.config.model.WardrobeConfigModel;
import continued.hideaway.mod.feat.rendering.screen.util.ButtonOptionWidget;
import continued.hideaway.mod.feat.rendering.screen.util.ButtonOutfitOptionWidget;
import continued.hideaway.mod.feat.wardrobe.Wardrobe;
import continued.hideaway.mod.feat.wardrobe.WardrobeOutfit;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

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
