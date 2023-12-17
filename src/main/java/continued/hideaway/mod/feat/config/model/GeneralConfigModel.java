package continued.hideaway.mod.feat.config.model;

public enum GeneralConfigModel {
    HIDE_COSMETIC (
            "false",
            "config.hp-config.general.hide_cosmetics"
    ),
    HIDE_LEFT_HAND (
            "false",
            "config.hp-config.general.hide_left_hand"
    ),
    DISCORD_RPC (
            "true",
            "config.hp-config.general.discord_rpc"
    ),
    EXP_PERCENT (
            "true",
            "config.hp-config.general.xp_percent"
    ),
    ACTIVITY_TIMER (
            "true",
            "config.hp-config.general.activity_timer"
    ),
    INVENTORY_RARITIES (
            "true",
            "config.hp-config.general.inventory_rarities"
    ),
    AUTO_SELL (
            "false",
            "config.hp-config.general.auto_sell"
    );

    public String value;
    public final String name;

    GeneralConfigModel(String value, String name) {
        this.name = name;
        this.value = value;
    }
}
