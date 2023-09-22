package plus.hideaway.mod.mixins;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import plus.hideaway.mod.HideawayPlus;
import plus.hideaway.mod.Prompt;
import plus.hideaway.mod.mixins.accessors.GameMenuScreenAccessor;
import plus.hideaway.mod.mixins.accessors.ScreenAccessor;

@Mixin(value=GameMenuScreen.class, priority = 999)
public class GameMenuScreenMixin {

    private static final Text RETURN_TO_GAME_TEXT = Text.translatable("menu.returnToGame");
    private static final Text ADVANCEMENTS_TEXT = Text.translatable("gui.advancements");
    private static final Text STATS_TEXT = Text.translatable("gui.stats");
    private static final Text SEND_FEEDBACK_TEXT = Text.translatable("menu.sendFeedback");
    private static final Text REPORT_BUGS_TEXT = Text.translatable("menu.reportBugs");
    private static final Text OPTIONS_TEXT = Text.translatable("menu.options");
    private static final Text SHARE_TO_LAN_TEXT = Text.translatable("menu.shareToLan");
    private static final Text PLAYER_REPORTING_TEXT = Text.translatable("menu.playerReporting");
    private static final Text RETURN_TO_MENU_TEXT = Text.translatable("menu.returnToMenu");
    private static final Text DISCONNECT_TEXT = Text.translatable("menu.disconnect");

    /**
     * @author Skye Redwood
     * @reason Adding extra buttons to pause menu
     *
     * God, this method is an absolute fucking mess
     */
    @Overwrite
    public void initWidgets() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(ButtonWidget.builder(RETURN_TO_GAME_TEXT, button -> {
            MinecraftClient.getInstance().setScreen(null);
            MinecraftClient.getInstance().mouse.lockCursor();
        }).width(204).build(), 2, gridWidget.copyPositioner().marginTop(50));
        adder.add(((GameMenuScreenAccessor) (Object) this).createButton(ADVANCEMENTS_TEXT, () -> new AdvancementsScreen(MinecraftClient.getInstance().player.networkHandler.getAdvancementHandler())));
        adder.add(((GameMenuScreenAccessor) (Object) this).createButton(STATS_TEXT, () -> new StatsScreen(((GameMenuScreen) (Object) this), MinecraftClient.getInstance().player.getStatHandler())));
        adder.add(((GameMenuScreenAccessor) (Object) this).createUrlButton(SEND_FEEDBACK_TEXT, SharedConstants.getGameVersion().isStable() ? "https://aka.ms/javafeedback?ref=game" : "https://aka.ms/snapshotfeedback?ref=game"));
        adder.add(((GameMenuScreenAccessor) (Object) this).createUrlButton(REPORT_BUGS_TEXT, (String)"https://aka.ms/snapshotbugs?ref=game")).active = !SharedConstants.getGameVersion().getSaveVersion().isNotMainSeries();
        adder.add(((GameMenuScreenAccessor) (Object) this).createButton(OPTIONS_TEXT, () -> new OptionsScreen(((GameMenuScreen) (Object) this), MinecraftClient.getInstance().options)));
        if (MinecraftClient.getInstance().isIntegratedServerRunning() && !MinecraftClient.getInstance().getServer().isRemote()) {
            adder.add(((GameMenuScreenAccessor) (Object) this).createButton(SHARE_TO_LAN_TEXT, () -> new OpenToLanScreen(((GameMenuScreen) (Object) this))));
        } else {
            adder.add(((GameMenuScreenAccessor) (Object) this).createButton(PLAYER_REPORTING_TEXT, SocialInteractionsScreen::new));
        }

        if (HideawayPlus.connected()) {
            // literally the only thing I added :sob:
            adder.add(ButtonWidget.builder(Text.literal("Hideaway+ Settings"), button -> {
                button.active = false;
                // HideawayPlus.config().show();
                Prompt.trace("I hate cloth config (maybe?)");
            }).width(204).build(), 2);
        }

        Text text = MinecraftClient.getInstance().isInSingleplayer() ? RETURN_TO_MENU_TEXT : DISCONNECT_TEXT;
        @Nullable ButtonWidget exitButton = adder.add(ButtonWidget.builder(text, button -> {
            button.active = false;
            MinecraftClient.getInstance().getAbuseReportContext().tryShowDraftScreen(MinecraftClient.getInstance(), ((GameMenuScreen) (Object) this), ((GameMenuScreenAccessor) (Object) this)::disconnect, true);
        }).width(204).build(), 2);
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, ((ScreenAccessor) (Object) this).getWidth(), ((ScreenAccessor) (Object) this).getHeight(), 0.5f, 0.25f);
        gridWidget.forEachChild((foo) -> {
            ((ScreenAccessor) (Object) this).addDrawableChild(foo);
        });
    }

}
