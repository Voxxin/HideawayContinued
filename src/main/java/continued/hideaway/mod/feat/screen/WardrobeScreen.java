package continued.hideaway.mod.feat.screen;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.ext.EntityAccessor;
import continued.hideaway.mod.feat.screen.util.RowWidget;
import continued.hideaway.mod.feat.wardrobe.Wardrobe;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.Rotation;

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
        this.addRenderableWidget(new RowWidget(HideawayPlus.client().screen.width/2 , HideawayPlus.client().screen.height - 15, 150, 17, Component.translatable("widget.model_slider.title")));
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void onClose() {
        HideawayPlus.player().connection.send(new ServerboundPlayerInputPacket(0, 0, false, true));
        HideawayPlus.client().setScreen(null);
    }
}
