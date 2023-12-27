package continued.hideaway.mod.mixins;

import continued.hideaway.mod.util.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin for preventing items from being moved inside custom inventories.
 * This is done to improve using menus, as there won't be any flickering when clicking on buttons anymore.
 */
@Mixin(Slot.class)
public abstract class InventorySlotMixin {

    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "mayPickup", at = @At(value = "HEAD"), cancellable = true)
    public void mayPickup(final Player player, final CallbackInfoReturnable<Boolean> cir) {
        final ItemStack itemStack = getItem();
        if (itemStack == null) return;

        final CompoundTag tag = itemStack.getTag();
        if (tag == null) return;

        final CompoundTag bukkit = tag.getCompound(Constants.PUBLIC_BUKKIT_VALUES);
        if (tag.getInt("CustomModelData") != 1 || !bukkit.contains(Constants.hideawayId("inventory-do-not-save"))) return;

        cir.setReturnValue(false);
    }
}