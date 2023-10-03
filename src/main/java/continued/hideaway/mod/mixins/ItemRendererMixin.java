package continued.hideaway.mod.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import continued.hideaway.mod.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.ItemRenderer;
import continued.hideaway.mod.HideawayPlus;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        if ((minecraft.player != null && displayContext != ItemDisplayContext.GUI && HideawayPlus.connected())) {
            CompoundTag stackNbt = itemStack.getTagElement(Constants.PUBLIC_BUKKIT_VALUES);

            if (stackNbt != null) {
                String stackId = stackNbt.getString(Constants.hideawayId("random"));

                if (HideawayPlus.config().hideCosmetics() || minecraft.options.getCameraType().isFirstPerson()) {
                    CompoundTag playerChestNbt = minecraft.player.getItemBySlot(EquipmentSlot.CHEST).getTagElement(Constants.PUBLIC_BUKKIT_VALUES);
                    if (playerChestNbt != null) {
                        String itemId = playerChestNbt.getString(Constants.hideawayId("random"));

                        if (stackId.equals(itemId)) {
                            ci.cancel();
                        }
                    }
                }

                if (HideawayPlus.config().hideCosmetics()) {
                    CompoundTag playerHeadNbt = minecraft.player.getItemBySlot(EquipmentSlot.HEAD).getTagElement(Constants.PUBLIC_BUKKIT_VALUES);
                    if (playerHeadNbt != null) {
                        String itemId = playerHeadNbt.getString(Constants.hideawayId("random"));

                        if (stackId.equals(itemId)) {
                            ci.cancel();
                        }
                    }
                }
            }
        }
    }
}
