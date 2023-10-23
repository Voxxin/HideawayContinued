package continued.hideaway.mod.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticValues {
    public static int shopIterationNum = 0;
    public static boolean shopScreenWasFilled = false;
    public static List<String> friends = new ArrayList<>();
    public static boolean friendsCheck = false;

    public static Map<String, String> users = new ConcurrentHashMap<>();
    public static List<String> devs = new ArrayList<>();
    public static List<String> teamMembers = new ArrayList<>();
    public static List<String> translators = new ArrayList<>();
}
