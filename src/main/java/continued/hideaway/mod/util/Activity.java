package continued.hideaway.mod.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public enum Activity {
    BREAKFAST (
            20,
            "Breakfast",
            "\uE013"
    ),
    MINIGAME (
            25,
            "Random minigame",
            "\uE011"
    ),
    POOL_PARTY (
            30,
            "Pool Party",
            "\uE014"
    ),
    MINIGAME2 (
            35,
            "Random minigame",
            "\uE011"
    ),
    BONFIRE (
            50,
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

    public static Activity getNext() {
        int minutes = Instant.now().atZone(ZoneOffset.UTC).getMinute();
        int distance = Math.abs(values[0].time - minutes);
        int idx = 0;
        for (int c = 1; c < values.length; c++){
            int cdistance = Math.abs(values[c].time - minutes);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        if (values[idx].time <= minutes) {
            return values[idx].getFollowing();
        } else {
            return values[idx];
        }
    }

    public Component getIcon() {
        return Component.literal(this.character).setStyle(Style.EMPTY.withFont(
                new ResourceLocation("hideaway_plus:text")
        )).withStyle(ChatFormatting.WHITE);
    }

    public String timeUntil() {
        Duration left;
        ZonedDateTime now = Instant.now().atZone(ZoneOffset.UTC);

        if (now.getMinute() > this.time) {
            left = Duration.between(now.withMinute(0), now.withMinute(this.time).withSecond(0));
        } else {
            left = Duration.between(now, now.withMinute(this.time).withSecond(0));
        }
        return left.toMinutes() + "m " + left.toSecondsPart() + "s";
    }

    Activity(int time, String name, String character) {
        this.time = time;
        this.name = name;
        this.character = character;
    }
}
