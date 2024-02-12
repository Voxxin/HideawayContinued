package continued.hideaway.mod.feat.config.model;

public enum GeneralConfigModel {
    HIDE_COSMETIC (
            false,
            "config.hp.general.hide_cosmetics",
            "boolean"
    ),
    HIDE_LEFT_HAND (
            false,
            "config.hp.general.hide_left_hand",
            "boolean"
    ),
    DISCORD_RPC (
            true,
            "config.hp.general.discord_rpc",
            "boolean"
    ),
    XP_PERCENT_OR_AMOUNT_ENABLED (
            true,
            "config.hp.general.xp_level_improvements",
            "boolean"
    ),
    XP_LVL_TYPE (
            "percentage",
            "config.hp.general.xp_level_type",
            "string"
    ),
    EXP_AMOUNT (
            true,
            "config.hp.general.xp_percent",
            "boolean"
    ),
    ACTIVITY_TIMER (
            true,
            "config.hp.general.activity_timer",
            "boolean"
    ),
    INVENTORY_RARITIES (
            true,
            "config.hp.general.inventory_rarities",
            "boolean"
    ),
    AUTO_SELL (
            false,
            "config.hp.general.auto_sell",
            "boolean"
    );

    public Object value;
    public final String name;
    public String type;

    GeneralConfigModel(Object value, String name, String type) {
        this.name = name;
        this.value = value;
    }

    public boolean asBoolean() {
        return (boolean) value;
    }

    public int asInt() {
        return (int) value;
    }

    public String asString() {
        return (String) value;
    }
}