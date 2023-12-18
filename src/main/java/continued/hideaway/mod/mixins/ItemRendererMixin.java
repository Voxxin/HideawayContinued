package continued.hideaway.mod.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import continued.hideaway.mod.feat.config.model.GeneralConfigModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.ItemRenderer;
import continued.hideaway.mod.HideawayPlus;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        if (displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND && HideawayPlus.connected() && GeneralConfigModel.HIDE_LEFT_HAND.value) {
            ci.cancel();
        }
    }
}