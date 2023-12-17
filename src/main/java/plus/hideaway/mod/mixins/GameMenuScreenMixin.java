package plus.hideaway.mod.mixins;

import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import plus.hideaway.mod.HideawayPlus;
import plus.hideaway.mod.Prompt;

@Mixin(GameMenuScreen.class)
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
    @Inject(method = "initWidgets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget;refreshPositions()V", shift = At.Shift.BY, by = -2), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void initWidgets(CallbackInfo ci, GridWidget gridWidget, GridWidget.Adder adder, Text text) {
        if (HideawayPlus.connected()) {
            // literally the only thing I added :sob:
            adder.add(ButtonWidget.builder(Text.literal("Hideaway+ Settings"), button -> {
                button.active = false;
                // HideawayPlus.config().show();
                Prompt.trace("I hate cloth config (maybe?)");
            }).width(204).build(), 2);
        }
    }

}
