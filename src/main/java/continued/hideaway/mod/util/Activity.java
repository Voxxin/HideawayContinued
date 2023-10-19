package continued.hideaway.mod.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public enum Activity {
    BREAKFAST (
            8000,
            "Breakfast",
            "\uE013"
    ),
    MINIGAME (
            10000,
            "Random minigame",
            "\uE011"
    ),
    POOL_PARTY (
            12000,
            "Pool Party",
            "\uE014"
    ),
    MINIGAME2 (
            14000,
            "Random minigame",
            "\uE011"
    ),
    BONFIRE (
            20000,
            "Bonfire",
            "\uE012"
    );

    public final int time;
    public final String name;
    public final String character;
    private static final Activity[] values = values();

    public Activity getFollowing() {
        return values[(this.ordinal() + 1) % values.length];
    }

    public static Activity getNext(int time) {
        int distance = Math.abs(values[0].time - time);
        int idx = 0;
        for(int c = 1; c < values.length; c++){
            int cdistance = Math.abs(values[c].time - time);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        if (values[idx].time <= time) {
            return values[idx].getFollowing();
        }
        return values[idx];
    }
    public Component getIcon() {
        return Component.literal(this.character).setStyle(Style.EMPTY.withFont(
                new ResourceLocation("hideaway_plus:text")
        )).withStyle(ChatFormatting.WHITE);
    }

    Activity(int time, String name, String character) {
        this.time = time;
        this.name = name;
        this.character = character;
    }
}
