package continued.hideaway.mod.feat.config.model;

public enum WardrobeConfigModel {

    DISABLE_WARDROBE_SONG (
            false,
            "config.hp-config.wardrobe.disable_wardrobe_song"
    ),
    DISABLE_CHAT (
            false,
            "config.hp-config.wardrobe.disable_chat"
    );

    public Boolean value;
    public final String name;

    WardrobeConfigModel(boolean value, String name) {
        this.name = name;
        this.value = value;
    }
}
