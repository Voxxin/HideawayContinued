package plus.hideaway.mod.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import plus.hideaway.mod.HideawayPlus;
import plus.hideaway.mod.util.Chars;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final HashMap<Text, Integer> activeToasts = new HashMap<>();

    @Inject(at = @At("HEAD"), method = "render")
    public void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (HideawayPlus.config().jukebox()) {
            if (HideawayPlus.jukebox().currentTrack != null) {
                context.drawTextWithShadow(
                        MinecraftClient.getInstance().textRenderer,
                        Text.empty()
                            .append(Chars.disc())
                            .append(Text.literal("Now playing: " + HideawayPlus.jukebox().currentTrack.name)),
                        10, 10, 0xffffff
                );
            }
        }

//
//        DISABLED FOR NOW - PERFORMANCE ISSUES
//
//        for (Map.Entry<Text, Integer> entry : activeToasts.entrySet()) {
//            if (entry.getValue() >= 300) {
//                activeToasts.remove(entry.getKey());
//            } else activeToasts.replace(entry.getKey(), entry.getValue() + 1);
//        }
//
//        for (Text t : HideawayPlus.toastStack()) {
//            HideawayPlus.toastStack().remove(t);
//            activeToasts.put(t, 0);
//        }
//
//        int yCounter = 10;
//        for (Text activeToast : activeToasts.keySet()) {
//            DrawableHelper.drawTextWithShadow(
//                matrices,
//                MinecraftClient.getInstance().textRenderer,
//                activeToast,
//                10, yCounter, 0xff5555
//            );
//
//            yCounter += 13;
//        }
    }
}
