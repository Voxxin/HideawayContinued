package continued.hideaway.mod.feat.config;

public enum ModConfigModel {
    HIDE_COSMETIC (
            false,
            "hideCosmetics"
    ),
    DISCORD_RPC (
            true,
            "discordRPC"
    ),
    INVENTORY_RARITIES (
            true,
            "inventoryRarities"
    ),
    AUTO_SELL (
            false,
            "autoSell"
    ),
    NO_AMBIENT_SOUNDS (
            true,
            "noAmbientSounds"
    ),
    NO_ACTIVITY_SONGS (
            true,
            "noActivitySongs"
    );

    public Boolean value;
    final String name;

    ModConfigModel(boolean value, String name) {
        this.name = name;
        this.value = value;
    }
}
