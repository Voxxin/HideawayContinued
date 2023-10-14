package continued.hideaway.mod.mixins;

import continued.hideaway.mod.feat.config.model.WardrobeConfigModel;
import continued.hideaway.mod.feat.wardrobe.WardrobeUtil;
import continued.hideaway.mod.util.DisplayNameUtil;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    @ModifyVariable(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V", argsOnly = true)
    private Component addMessageIcons(Component message) {
        String playerName = DisplayNameUtil.nameFromChatMessage(message.getString());
        return DisplayNameUtil.withBadges((MutableComponent) message, playerName, true);
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(CallbackInfo ci){
        if (WardrobeUtil.isAtWardrobe() && WardrobeConfigModel.DISABLE_CHAT.value) ci.cancel();
    }
}