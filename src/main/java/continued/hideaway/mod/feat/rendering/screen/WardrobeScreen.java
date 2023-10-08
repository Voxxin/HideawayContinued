package continued.hideaway.mod.feat.rendering.screen;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.rendering.screen.util.ButtonScreenWidget;
import continued.hideaway.mod.feat.rendering.screen.util.RotationSliderWidget;
import continued.hideaway.mod.feat.wardrobe.Wardrobe;
import continued.hideaway.mod.feat.wardrobe.WardrobeOutfit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public class WardrobeScreen extends Screen {
    private final Minecraft minecraft = HideawayPlus.client();
    private final Font font = this.minecraft.font;

    public WardrobeScreen() {
        super(Component.nullToEmpty(""));
    }

    @Override
    public void init() {
        super.init();
        assert HideawayPlus.client().screen != null;
        int ScreenButtonsSize = 40;

        this.addRenderableWidget(new RotationSliderWidget(HideawayPlus.client().screen.width/2 , HideawayPlus.client().screen.height - 15, 170, 20, Component.translatable("widget.model_slider.title")));
        this.addRenderableWidget(new ButtonScreenWidget(HideawayPlus.client().screen.width - 20 - ScreenButtonsSize, HideawayPlus.client().screen.height / 2, ScreenButtonsSize, ScreenButtonsSize, "widget.model_slider.title", new WardrobeOutfitScreen()));
        this.addRenderableWidget(new ButtonScreenWidget(HideawayPlus.client().screen.width - 20 - ScreenButtonsSize, HideawayPlus.client().screen.height / 2 + (ScreenButtonsSize * 2), ScreenButtonsSize, ScreenButtonsSize, "widget.model_slider.title", new WardrobeSkinScreen()));
        this.addRenderableWidget(new ButtonScreenWidget(HideawayPlus.client().screen.width - 20 - ScreenButtonsSize, HideawayPlus.client().screen.height / 2 - (ScreenButtonsSize * 2), ScreenButtonsSize, ScreenButtonsSize, "widget.model_slider.title", new WardrobeConfigScreen()));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void onClose() {
//        HideawayPlus.player().connection.send(new ServerboundPlayerInputPacket(0, 0, false, true));
        HideawayPlus.client().setScreen(null);
        Player wardrobePlayer = Wardrobe.wardrobePlayer;
        Wardrobe.outfit = new WardrobeOutfit("","", "", Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.HEAD), Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.CHEST), Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.OFFHAND));
        Wardrobe.applyOutfit();
    }
}
