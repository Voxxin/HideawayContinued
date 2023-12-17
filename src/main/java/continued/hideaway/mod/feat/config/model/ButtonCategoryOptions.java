package continued.hideaway.mod.feat.config.model;

public enum ButtonCategoryOptions {
    GENERAL(
            "config.hp-config.category.general"
    ),
    SOUNDS(
            "config.hp-config.category.sounds"
    );

    public final String key;

    ButtonCategoryOptions(String key) {
        this.key = key;
    }
}