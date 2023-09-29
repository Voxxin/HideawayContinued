package continued.hideaway.mod.mixins;

import continued.hideaway.mod.HideawayPlus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Inject(at = @At("HEAD"), method = "close")
    public void close(CallbackInfo ci) {
        // HideawayPlus.ws().end();
    }

    @Inject(at = @At("HEAD"), method = "setScreen", cancellable = true)
    public void hideAdvancements(Screen screen, CallbackInfo ci) {
        if (HideawayPlus.connected() && screen instanceof AdvancementsScreen) {
            ci.cancel();
        }
    }
}
