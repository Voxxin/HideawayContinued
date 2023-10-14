package continued.hideaway.mod.mixins;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.location.Location;
import continued.hideaway.mod.feat.wardrobe.Wardrobe;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public abstract class ArmourStandMobMixin {
    @Shadow
    public abstract void setItemSlot(EquipmentSlot slot, ItemStack stack);

    @Shadow
    public abstract void kill();

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @Unique
    private ItemStack oldHeadStack = null;

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo ci) {
        if (!HideawayPlus.connected()) return;

        ArmorStand armorStand = (ArmorStand) (Object) this;
        boolean hasCosmetic = armorStand.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.LEATHER_HORSE_ARMOR;
        Vec3 pos = armorStand.position();
        if (pos.closerThan(new Vec3(71.5f, 5f, -135.5f), 5)
                && hasCosmetic
                && Wardrobe.wardrobePlayer != null
                && Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.CHEST) != this.getItemBySlot(EquipmentSlot.HEAD)) {
            Wardrobe.wardrobePlayer.setItemSlot(EquipmentSlot.CHEST, this.getItemBySlot(EquipmentSlot.HEAD));
            this.kill();
        } else if (!pos.closerThan(new Vec3(71.5f, 5f, -135.5f), 5)
                        && hasCosmetic) {
            this.kill();
        }
    }

}
