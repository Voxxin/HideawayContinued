package continued.hideaway.mod.feat.wardrobe;

import continued.hideaway.mod.util.StaticValues;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

import static continued.hideaway.mod.util.ParseItemName.getItemId;

public class OutfitUtil {
    public static void applyOutfit(int wardrobeIndex) {
        if (StaticValues.wardrobeOutfits.isEmpty()) return;
        WardrobeOutfit outfit = StaticValues.wardrobeOutfits.get(wardrobeIndex);
        Wardrobe.wardrobePlayer.setItemSlot(EquipmentSlot.HEAD, outfit.head);
        Wardrobe.wardrobePlayer.setItemSlot(EquipmentSlot.CHEST, outfit.chest);
        Wardrobe.wardrobePlayer.setItemSlot(EquipmentSlot.OFFHAND, outfit.holdable);
    }

    public static String applyOutfitName(int wardrobeIndex) {
        if (StaticValues.wardrobeOutfits.isEmpty()) return "";
        else return StaticValues.wardrobeOutfits.get(wardrobeIndex).title;
    }

    public static boolean outfitExists() {
        return StaticValues.wardrobeOutfits.stream().anyMatch(
                outfit -> getItemId(outfit.head).equals(getItemId(Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.HEAD)))
                        && getItemId(outfit.chest).equals(getItemId(Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.CHEST)))
                        && getItemId(outfit.holdable).equals(getItemId(Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.OFFHAND)))
        );
    }

    public static boolean validOutfit(String title) {
        if (Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.HEAD).getItem() == Items.AIR && Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.CHEST).getItem() == Items.AIR && Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.OFFHAND).getItem() == Items.AIR) return false;
        return !title.isEmpty();
    }

    public static WardrobeOutfit getOutfit() {
        if (StaticValues.wardrobeOutfits.isEmpty()) return null;
        return StaticValues.wardrobeOutfits.stream()
                .filter(outfit ->
                        getItemId(outfit.head).equals(getItemId(Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.HEAD)))
                        && getItemId(outfit.chest).equals(getItemId(Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.CHEST)))
                        && getItemId(outfit.holdable).equals(getItemId(Wardrobe.wardrobePlayer.getItemBySlot(EquipmentSlot.OFFHAND)))
                )
                .findFirst()
                .orElse(null);
    }

}
