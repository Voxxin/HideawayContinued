package continued.hideaway.mod.feat.config.model;

public enum ModConfigModel {
    HIDE_COSMETIC (
            false,
            "config.hp-config.general.hide_cosmetics"
    ),
    DISCORD_RPC (
            true,
            "config.hp-config.general.discord_rpc"
    ),
    EXP_PERCENT (
            true,
            "config.hp-config.general.xp_percent"
    ),
    ACTIVITY_TIMER (
            true,
            "config.hp-config.general.activity_timer"
    ),
    INVENTORY_RARITIES (
            true,
            "config.hp-config.general.inventory_rarities"
    ),
    AUTO_SELL (
            false,
            "config.hp-config.general.auto_sell"
    ),
    NO_AMBIENT_SOUNDS (
            false,
            "config.hp-config.general.no_ambient_sounds"
    ),
    NO_ACTIVITY_SONGS (
            false,
            "config.hp-config.general.no_activity_songs"
    );

    public Boolean value;
    public final String name;

    ModConfigModel(boolean value, String name) {
        this.name = name;
        this.value = value;
    }
}
