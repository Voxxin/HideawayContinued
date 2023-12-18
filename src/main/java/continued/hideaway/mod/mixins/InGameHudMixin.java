package continued.hideaway.mod.mixins;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.model.GeneralConfigModel;
import continued.hideaway.mod.feat.ext.InGameHudAccessor;
import continued.hideaway.mod.feat.location.Location;
import continued.hideaway.mod.util.Activity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Gui.class)
public abstract class InGameHudMixin implements InGameHudAccessor {

    @Shadow private int screenWidth;

    @Shadow public abstract Font getFont();

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Nullable private Component overlayMessageString;

    @Shadow @Nullable private Component title;

    @Shadow @Nullable private Component subtitle;

    @Shadow @Final private DebugScreenOverlay debugOverlay;

    @Inject(at = @At("HEAD"), method = "render")
    public void onRender(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        if (GeneralConfigModel.ACTIVITY_TIMER.value && !this.debugOverlay.showDebugScreen() && HideawayPlus.connected()) {
            int color = FastColor.ARGB32.color(100, 0, 0, 0);
            int padding = 3;
            Activity activity = Activity.getNext();
            Font font = minecraft.font;
            Component text = Component.empty()
                    .append(activity.getIcon())
                    .append(Component.literal(" " + activity.name + " in " + activity.timeUntil()));
            guiGraphics.fill(0, 30 - padding, 10 + padding + font.width(text), 30 + padding - 1 + font.lineHeight, color);
            guiGraphics.drawString(font, text, 10,30, 0xffffff);
        }

        if (!this.debugOverlay.showDebugScreen() && HideawayPlus.connected() && minecraft.player.getInventory().getFreeSlot() == -1) {
            int color = FastColor.ARGB32.color(100, 0, 0, 0);
            int padding = 3;
            int yLevel = GeneralConfigModel.ACTIVITY_TIMER.value ? 45 : 30;
            Font font = minecraft.font;
            Component text = Component.empty()
                    .append(Component.literal("\uE015").setStyle(Style.EMPTY.withFont(new ResourceLocation("hideaway_plus:text"))))
                    .append(Component.literal(" Full inventory").withStyle(ChatFormatting.RED));
            guiGraphics.fill(0, yLevel - padding, 10 + padding + font.width(text), yLevel + padding - 1 + font.lineHeight, color);
            guiGraphics.drawString(font, text, 10,yLevel, 0xffffff);
        }
    }

    @Inject(at = @At("HEAD"), method = "renderExperienceBar", cancellable = true)
    public void renderExperienceBar(GuiGraphics guiGraphics, int x, CallbackInfo ci) {
        if (HideawayPlus.connected() && HideawayPlus.location() == Location.WARDROBE_WHEEL) {
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceBar",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I", ordinal = 1, shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD,
            slice = @Slice(
                    from = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I", ordinal = 1))
    )
    public void experienceBarPercent(GuiGraphics guiGraphics, int x, CallbackInfo ci, int i, String string, int textSize, int textPos) {
        if (HideawayPlus.connected() && GeneralConfigModel.EXP_PERCENT.value) {
            if (overlayMessageString != null && !overlayMessageString.getString().contains("\uE2C3")) return;

            string = (Math.round(this.minecraft.player.experienceProgress * 10000) / 100.0) + "%";
            textSize = (this.screenWidth - this.getFont().width(string)) / 2;

            textPos = textPos - 14;

            guiGraphics.drawString(this.getFont(), string, textSize + 1, textPos + 1, 0, true);
            guiGraphics.drawString(this.getFont(), string, textSize + 1, textPos, 8453920, true);
        }
    }

    @Override
    public Component hp$getOverlayMessage() {
        return this.overlayMessageString;
    }

    @Override
    public Component hp$getTitleMessage() {
        return this.title;
    }

    @Override
    public Component hp$getSubtitleMessage() {
        return this.subtitle;
    }

    @Override
    public float hp$getExperiencePoints() {
        return this.minecraft.player.experienceProgress;
    }

    @Override
    public int hp$getExperienceLevel() {
        return this.minecraft.player.experienceLevel;
    }
}
