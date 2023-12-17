package continued.hideaway.mod.util;

import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisplayNameUtil {
    public static String ignFromDisplayName(String content) {
        Pattern pattern = Pattern.compile("[\\w]+");
        Matcher matcher = pattern.matcher(content);
        String username = matcher.find() ? matcher.group(0) : null;
        if (username == null || username.isEmpty()) {
            return content;
        } else return username;
    }

    public static String nameFromChatMessage(String chatMessage) {
        String username = chatMessage.replaceAll(":(.*)", "");
        username = username.replaceAll("^\\S+ ", "");
        if (username.isEmpty()) {
            return "";
        } else return username;
    }

    public static MutableComponent withBadges(MutableComponent text, String playerName, boolean tooltip) {
        String playerID = "";
        MutableComponent newComponent = MutableComponent.create(ComponentContents.EMPTY);

        for (Map.Entry<String, String> entry : StaticValues.users.entrySet()) {
            if (entry.getValue().equals(playerName)) {
                playerID = entry.getKey();
            }
        }

        if (StaticValues.friends.contains(playerName)) Chars.FRIEND.addBadge(newComponent, tooltip);

        if (StaticValues.devs.contains(playerID)) Chars.DEV.addBadge(newComponent, tooltip);
        else if (StaticValues.teamMembers.contains(playerID)) Chars.TEAM.addBadge(newComponent, tooltip);
        else if (StaticValues.translators.contains(playerID)) Chars.TRANSLATOR.addBadge(newComponent, tooltip);
        else if (StaticValues.users.containsKey(playerID)) Chars.USER.addBadge(newComponent, tooltip);

        return (tooltip) ? newComponent.append(text) : text.append(" ").append(newComponent);
    }
}
