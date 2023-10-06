package continued.hideaway.mod.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ParseItemName {

    public static String getItemName(ItemStack itemstack) {
        CompoundTag itemCompounds = itemstack.getTag();

        if (itemCompounds != null && itemCompounds.getAsString().contains(Constants.PUBLIC_BUKKIT_VALUES)) {
            JsonObject bukkitJO = JsonParser.parseString(itemCompounds.getCompound(Constants.PUBLIC_BUKKIT_VALUES).getAsString()).getAsJsonObject();
            for (String key : bukkitJO.keySet()) {
                if (key.contains(Constants.hideawayId("gameplay_item_id"))) {
                    return bukkitJO.get(key).getAsString();
                }
            }
        }

        System.out.println(itemCompounds.toString());

        return null;
    }

    public static String getItemId(ItemStack itemstack) {
        if (itemstack == null || !itemstack.hasTag()) return "";
        CompoundTag itemCompounds = itemstack.getTag();

        if (itemCompounds != null && itemCompounds.getAsString().contains(Constants.PUBLIC_BUKKIT_VALUES)) {
            JsonObject bukkitJO = JsonParser.parseString(itemCompounds.getCompound(Constants.PUBLIC_BUKKIT_VALUES).getAsString()).getAsJsonObject();
            for (String key : bukkitJO.keySet()) {
                if (key.contains(Constants.hideawayId("item_id"))) {
                    return bukkitJO.get(key).getAsString();
                }
            }
        }
        return "";

    }
}
