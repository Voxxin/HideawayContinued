package plus.hideaway.mod.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import plus.hideaway.mod.HideawayPlus;

@Mixin(AdvancementsScreen.class)
public class AdvancementScreenMixin {

    /**
     * @author Skye Redwood
     * @reason Advancements break Hideaway ew
     */
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (HideawayPlus.connected()) {
            ci.cancel();
        }
    }

}
