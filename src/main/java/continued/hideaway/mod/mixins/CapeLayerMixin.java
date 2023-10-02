package continued.hideaway.mod.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.layers.CapeLayer;
import continued.hideaway.mod.HideawayPlus;

@Mixin(CapeLayer.class)
public class CapeLayerMixin {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
        if (HideawayPlus.connected()) {
            if (!HideawayPlus.config().hideCosmetics()) {
                ItemStack playerChestplate = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
                if (playerChestplate != ItemStack.EMPTY) {
                    info.cancel();
                }
            } else if (StaticValues.wardrobeEntity.contains(livingEntity.getStringUUID())) {
                if (!StaticValues.wardrobeArmourStand.isEmpty()) info.cancel();
            }
        }
    }
}
