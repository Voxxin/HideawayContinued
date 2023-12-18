package continued.hideaway.mod.feat.config.model;

public enum GeneralConfigModel {
    HIDE_COSMETIC (
            false,
            "config.hp.general.hide_cosmetics"
    ),
    HIDE_LEFT_HAND (
            false,
            "config.hp.general.hide_left_hand"
    ),
    DISCORD_RPC (
            true,
            "config.hp.general.discord_rpc"
    ),
    EXP_PERCENT (
            true,
            "config.hp.general.xp_percent"
    ),
    ACTIVITY_TIMER (
            true,
            "config.hp.general.activity_timer"
    ),
    INVENTORY_RARITIES (
            true,
            "config.hp.general.inventory_rarities"
    ),
    AUTO_SELL (
            false,
            "config.hp.general.auto_sell"
    ),
    AMBIENT_SOUNDS (
            true,
            "config.hp.general.ambient_sounds"
    ),
    ACTIVITY_SONGS (
            true,
            "config.hp.general.activity_songs"
    );

    public Boolean value;
    public final String name;

    GeneralConfigModel(boolean value, String name) {
        this.name = name;
        this.value = value;
    }
}
