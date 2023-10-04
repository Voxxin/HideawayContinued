package continued.hideaway.mod.feat.wardrobe;

import net.minecraft.world.item.ItemStack;

public class WardrobeOutfit {
    public String title;
    public String fileName;
    public String caseName;
    public ItemStack head;
    public ItemStack chest;
    public ItemStack holdable;

    public WardrobeOutfit(String title, String fileName, String caseName, ItemStack head, ItemStack chest, ItemStack holdable) {
        this.fileName = fileName;
        this.caseName = caseName;
        this.title = title;
        this.head = head;
        this.chest = chest;
        this.holdable = holdable;
    }
}
