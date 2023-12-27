package continued.hideaway.mod.feat.config.model;

public enum SoundConfigModel {
    AMBIENT_SOUNDS (
            true,
            "config.hp.sounds.ambient_sounds"
    ),
    ACTIVITY_SONGS (
            true,
            "config.hp.sounds.activity_songs"
    ),
    INSTRUMENT_SOUNDS (
            true,
            "config.hp.sounds.instrument_songs"
    ),
    UI_SOUNDS (
            true,
            "config.hp.sounds.ui_sounds"
    ),
    CHARACTER_SOUNDS (
            true,
            "config.hp.sounds.npc_sounds"
    ),
    DIALOGUE_SOUNDS (
            true,
            "config.hp.sounds.dialogue_sounds"
    ),
    ITEM_PICK_UP_SOUNDS (
            true,
            "config.hp.sounds.item_pick_up_sound"
    );

    public Boolean value;
    public final String name;

    SoundConfigModel(Boolean value, String name) {
        this.name = name;
        this.value = value;
    }
}