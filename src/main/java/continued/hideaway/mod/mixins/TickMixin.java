package continued.hideaway.mod.mixins;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.model.WardrobeConfigModel;
import continued.hideaway.mod.feat.wardrobe.WardrobeUtil;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class TickMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci){
        HideawayPlus.lifecycle().tick();
    }
}
