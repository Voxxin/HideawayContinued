package continued.hideaway.mod.mixins;

import com.mojang.authlib.GameProfile;
import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.model.ModConfigModel;
import continued.hideaway.mod.feat.ext.EntityAccessor;
import continued.hideaway.mod.feat.wardrobe.Wardrobe;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements EntityAccessor {
    @Final
    @Shadow
    protected static EntityDataAccessor<Byte> DATA_PLAYER_MODE_CUSTOMISATION;

    @Shadow public abstract void setItemSlot(EquipmentSlot slot, ItemStack stack);

    @Mutable
    @Shadow @Final private GameProfile gameProfile;

    @Shadow public abstract ItemStack eat(Level level, ItemStack food);

    @Unique private ItemStack oldHeadStack = null;
    @Unique private ItemStack oldChestplateStack = null;
    @Unique private boolean setLooking = false;
    @Unique private Player player = ((Player) (Object) this);
    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        if ((!StaticValues.wardrobeEntity.isEmpty() && StaticValues.wardrobeEntity.contains(player.getStringUUID())) || (Wardrobe.wardrobePlayer != null && Wardrobe.wardrobePlayer.getStringUUID().equals(player.getStringUUID()))) {
            if (HideawayPlus.client().player == null) return;
            Byte b = (HideawayPlus.client().player).getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION);
            player.getEntityData().set(DATA_PLAYER_MODE_CUSTOMISATION, b);
        }

        if (!(Wardrobe.wardrobePlayer != null && Wardrobe.wardrobePlayer.getStringUUID().equals(player.getStringUUID()))) {
            boolean hasHeadCosmetic = player.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HORSE_ARMOR;
            boolean hasChestCosmetic = player.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.LEATHER_HORSE_ARMOR;

            if (hasHeadCosmetic) oldHeadStack = player.getItemBySlot(EquipmentSlot.HEAD);
            if (hasHeadCosmetic && HideawayPlus.connected() && ModConfigModel.HIDE_COSMETIC.value)
                this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
            if (!hasHeadCosmetic && HideawayPlus.connected() && !ModConfigModel.HIDE_COSMETIC.value && oldHeadStack != null)
                this.setItemSlot(EquipmentSlot.HEAD, oldHeadStack);

            if (hasChestCosmetic) oldChestplateStack = player.getItemBySlot(EquipmentSlot.CHEST);
            if (hasChestCosmetic && HideawayPlus.connected() && ModConfigModel.HIDE_COSMETIC.value)
                this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            if (!hasChestCosmetic && HideawayPlus.connected() && !ModConfigModel.HIDE_COSMETIC.value && oldChestplateStack != null)
                this.setItemSlot(EquipmentSlot.CHEST, oldChestplateStack);
        }

        if (Wardrobe.wardrobePlayer != null && !setLooking && HideawayPlus.client().screen == null) {
            HideawayPlus.player().lookAt(EntityAnchorArgument.Anchor.EYES, Wardrobe.wardrobePlayer.position());
        }
        else if (Wardrobe.wardrobePlayer == null && setLooking && HideawayPlus.client().screen == null) setLooking = false;
        if (!StaticValues.playerRotationSet && Wardrobe.wardrobePlayer != null && Wardrobe.wardrobePlayer.getStringUUID().equals(player.getStringUUID())) ((EntityAccessor)Wardrobe.wardrobePlayer).hp$addRot(2.5F);
    }

    @Override
    public void hp$addRot(float rot) {
        float yaw = player.getYRot() + rot;
        float pitch = player.getXRot() + rot;
        yaw = Mth.wrapDegrees(yaw);
        pitch = Mth.wrapDegrees(pitch);

        player.setYBodyRot(yaw);
        player.setYHeadRot(yaw);
        player.setYRot(yaw);
    }

    @Override
    public void hp$setRot(float rot) {
        float yaw = Mth.wrapDegrees(rot);

        player.setYBodyRot(yaw);
        player.setYHeadRot(yaw);
        player.setYRot(yaw);
    }
}
