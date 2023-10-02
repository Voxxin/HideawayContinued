package continued.hideaway.mod.mixins;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.ext.InGameHudAccessor;
import continued.hideaway.mod.feat.location.Location;
import continued.hideaway.mod.feat.wardrobe.Wardrobe;
import continued.hideaway.mod.util.Chars;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
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

    @Inject(at = @At("HEAD"), method = "render")
    public void onRender(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        if (Wardrobe.wardrobePlayer != null) overlayMessageString = Component.empty();

        if (HideawayPlus.jukebox() != null && HideawayPlus.jukebox().currentTrack != null) {
            guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    Component.empty()
                            .append(Chars.disc())
                            .append(Component.literal("Now playing: " + HideawayPlus.jukebox().currentTrack.name)),
                    10, 10, 0xffffff, true
            );
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
        if (HideawayPlus.connected()) {
            if (overlayMessageString == null || !overlayMessageString.getString().contains("\uE2C3")) return;

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
