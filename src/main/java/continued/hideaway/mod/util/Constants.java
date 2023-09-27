package continued.hideaway.mod.util;

import net.minecraft.resources.ResourceLocation;

public class Constants {
	public static final String VERSION = "0.1.0-beta+build.1-1.20.1";
	public static final String MOD_ID = "hideaway_plus";
	public static final String MOD_NAME = "Hideaway+";
	
	public static final String PUBLIC_BUKKIT_VALUES = "PublicBukkitValues";
	
	public static String hideawayId(String path) {
		return new ResourceLocation("pixelhideawaycore", path).toString();
	}

	public static boolean MOD_MENU_PRESENT = false;
}
