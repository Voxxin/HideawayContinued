package continued.hideaway.mod.feat.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import continued.hideaway.mod.feat.config.model.GeneralConfigModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CustomChestLayer<T extends AbstractClientPlayer, M extends EntityModel<T>>
        extends RenderLayer<T, M> {

    private final float scaleX;
    private final float scaleY;
    private final float scaleZ;
    private final ItemInHandRenderer itemInHandRenderer;
    public CustomChestLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet, ItemInHandRenderer itemInHandRenderer) {
        this(renderer, modelSet, 1.0f, 1.0f, 1.0f, itemInHandRenderer);
    }

    public CustomChestLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet, float scaleX, float scaleY, float scaleZ, ItemInHandRenderer itemInHandRenderer) {
        super(renderer);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (GeneralConfigModel.HIDE_COSMETIC.value) return;
        ItemStack chestArmor = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestArmor.isEmpty()) return;

        poseStack.pushPose();
        poseStack.scale(this.scaleX, this.scaleY, this.scaleZ);
        CustomChestLayer.translateToBody(poseStack, player.isCrouching());
        this.itemInHandRenderer.renderItem(player, chestArmor, ItemDisplayContext.HEAD, false, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    public static void translateToBody(PoseStack poseStack, boolean crouching) {
        poseStack.translate(0.0f, -0.25f, 0.0f);
        if (crouching) {
            poseStack.mulPose(Axis.XP.rotationDegrees(30.0f));
            poseStack.translate(0.0f, 0.15f, -0.245f);
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        poseStack.scale(0.625f, -0.625f, -0.625f);
        poseStack.translate(0.0D, 2.195, 0.0D);
    }


}
