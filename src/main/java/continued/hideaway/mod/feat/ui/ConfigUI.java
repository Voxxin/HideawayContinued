package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.feat.config.HideawayPlusConfig;
import continued.hideaway.mod.feat.config.model.ModConfigModel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.MutableComponent;

import java.util.*;

public class ConfigUI extends Screen {
    private final Screen parent;

    public ConfigUI(Screen parent) {
        super(Component.translatable("config.hp-config.general.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        List<ModConfigModel> options = Arrays.asList(ModConfigModel.values());
        int spacingY = 25;
        int spacingX = 5;
        int startingY = 25;
        int availableSpace = this.height - 20 - spacingY;
        int totalHeight = options.size() * spacingY + startingY;
        int rows = 1;

        while (totalHeight / rows > availableSpace) {
            rows++;
        }

        int buttonWidth = this.width / (rows + 1) - spacingX;

        int currentColumn = 0;
        int currentRow = 0;
        for (int i = 0; i < options.size(); i++) {
            ModConfigModel configModel = options.get(i);
            MutableComponent label = Component.translatable(configModel.name).append(": ");

            if (currentColumn > (options.size() - 1) / rows) {
                currentRow++;
                currentColumn = 0;
            }

            int xPos = (rows > 1) ? this.width / (rows + 1) * (currentRow + 1) - buttonWidth / 2 : this.width / 2 - buttonWidth / 2;
            int yPos = startingY + spacingY * currentColumn;

            this.addRenderableWidget(Button.builder(label.copy().append(configModel.value ? "ON" : "OFF"), button -> {
                configModel.value = !configModel.value;
                HideawayPlusConfig.updateModConfig();
                button.setMessage(label.copy().append(configModel.value ? "ON" : "OFF"));
            }).bounds(xPos, yPos, buttonWidth, 20).build());

            currentColumn++;
        }
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> this.minecraft.setScreen(this.parent))
                .pos(this.width / 2 - 75, this.height - 20 - spacingY).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderDirtBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
