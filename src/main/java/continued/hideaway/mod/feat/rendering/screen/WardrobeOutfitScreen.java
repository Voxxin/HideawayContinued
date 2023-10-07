package continued.hideaway.mod.feat.rendering.screen;

import com.mojang.blaze3d.platform.InputConstants;
import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.HideawayPlusConfig;
import continued.hideaway.mod.feat.rendering.screen.util.ButtonScreenWidget;
import continued.hideaway.mod.feat.rendering.screen.util.OutfitItemsWidget;
import continued.hideaway.mod.feat.rendering.screen.util.RotationSliderWidget;
import continued.hideaway.mod.util.Constants;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.KeybindContents;

public class WardrobeOutfitScreen extends Screen {
    private int oldScreenHeight = HideawayPlus.client().screen.height;
    private int oldScreenWidth = HideawayPlus.client().screen.width;
    protected WardrobeOutfitScreen() {
        super(Component.nullToEmpty(""));
    }
    private OutfitItemsWidget input;

    @Override
    public void init() {
        super.init();
        assert HideawayPlus.client().screen != null;
        HideawayPlusConfig.init();

        input = new OutfitItemsWidget(oldScreenWidth, oldScreenHeight, Component.translatable("widget.model_slider.title"));
        input.setResponder(this::onEdited);

        this.addRenderableWidget(input);
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

    private void onEdited(String value) {
        this.input.setValue(value);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        onEdited(InputConstants.getKey(keyCode, scanCode).getDisplayName().getString());
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        double accumulatedDelta = 0.0;
        double smoothingFactor = 0.2;

        accumulatedDelta += delta;
        double smoothedDelta = accumulatedDelta * smoothingFactor;
        accumulatedDelta -= smoothedDelta;

        onEdited(String.valueOf(accumulatedDelta));
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
}
