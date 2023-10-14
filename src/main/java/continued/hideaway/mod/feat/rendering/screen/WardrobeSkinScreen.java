package continued.hideaway.mod.feat.rendering.screen;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.HideawayPlusConfig;
import continued.hideaway.mod.feat.rendering.screen.widget.OutfitItemsWidget;
import continued.hideaway.mod.feat.rendering.screen.widget.RotationSliderWidget;
import continued.hideaway.mod.feat.rendering.screen.widget.SkinsBarWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class WardrobeSkinScreen extends Screen {

    private SkinsBarWidget input;

    protected WardrobeSkinScreen() {
        super(Component.nullToEmpty(""));
    }

    @Override
    public void init() {
        super.init();
        assert HideawayPlus.client().screen != null;
        HideawayPlusConfig.init();

        input = new SkinsBarWidget(this.width, this.height, Component.translatable("widget.model_slider.title"));
        input.setResponder(this::onEdited);

        this.addRenderableWidget(new RotationSliderWidget(HideawayPlus.client().screen.width/2 ,36, 170, 20, Component.translatable("widget.model_slider.title")));
        this.addRenderableWidget(input);
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
