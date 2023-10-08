package continued.hideaway.mod.mixins;

import continued.hideaway.mod.feat.config.model.WardrobeConfigModel;
import continued.hideaway.mod.feat.wardrobe.WardrobeUtil;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(CallbackInfo ci){
        if (WardrobeUtil.isAtWardrobe() && WardrobeConfigModel.DISABLE_CHAT.value) ci.cancel();
    }
}
