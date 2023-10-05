package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.model.ModConfigModel;
import continued.hideaway.mod.feat.rendering.screen.util.ButtonOptionWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigUI extends Screen {
    private final String title;
    private final Screen parent;
    public ConfigUI(Screen parent) {
        super(Component.translatable("config.hp-config.general.title"));
        this.parent = parent;
        this.title = "config.hp-config.general.title";
    }

    @Override
    protected void init() {
        super.init();
        int spacing = 0;
        for (ModConfigModel configModel : ModConfigModel.values()) {
            assert HideawayPlus.client().screen != null;

            int startingPosX = 20;
            int startingPosY = ((HideawayPlus.client().screen.height) / 5) + (30 * spacing);

            this.addRenderableWidget(new ButtonOptionWidget(startingPosX, startingPosY, 20, 20, configModel));
            spacing++;
        }

    }

    @Override
    public void onClose() {
        HideawayPlus.client().setScreen(parent);
    }
}
