package continued.hideaway.mod.mixins;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.model.ModConfigModel;
import continued.hideaway.mod.feat.ext.EntityAccessor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements EntityAccessor {
    @Shadow public abstract void setItemSlot(EquipmentSlot slot, ItemStack stack);

    @Unique private ItemStack oldHeadStack = null;
    @Unique private ItemStack oldChestplateStack = null;
    @Unique private Player player = ((Player) (Object) this);
    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
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
}
