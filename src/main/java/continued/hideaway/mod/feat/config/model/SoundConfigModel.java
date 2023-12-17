package continued.hideaway.mod.feat.config.model;

public enum SoundConfigModel {
    AMBIENT_SOUNDS (
            "true",
            "config.hp-config.sounds.ambient_sounds"
    ),
    ACTIVITY_SONGS (
            "true",
            "config.hp-config.sounds.activity_songs"
    ),
    INSTRUMENT_SOUNDS (
            "false",
            "config.hp-config.sounds.instrument_songs"
    ),
    UI_SOUNDS (
            "false",
            "config.hp-config.sounds.ui_sounds"
    ),
    CHARACTER_SOUNDS (
            "false",
            "config.hp-config.sounds.character_sounds"
    ),
    DIALOGUE_SOUNDS (
            "false",
            "config.hp-config.sounds.dialogue_sounds"
    ),
    ITEM_PICK_UP_SOUNDS (
            "false",
            "config.hp-config.sounds.item_pick_up_sound"
    );

    public String value;
    public final String name;

    SoundConfigModel(String value, String name) {
        this.name = name;
        this.value = value;
    }
}
