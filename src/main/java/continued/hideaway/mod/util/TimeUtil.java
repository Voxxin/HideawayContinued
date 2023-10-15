package continued.hideaway.mod.util;

public class TimeUtil {
    public static int getGameTime(int ticks) {
        int time;

        while (ticks >= 24000) {
            ticks-=24000;
        }

        if (ticks >= 18000) {
            time = ticks-18000;
        }
        else {
            time = 6000+ticks;
        }
        return time;
    }

    public static String timeAsString(int time) {
        String suffix;
        if (time >= 13000) {
            time = time - 12000;
            suffix = " PM";
        }
        else {
            if (time >= 12000) {
                suffix = " PM";
            }
            else {
                suffix = " AM";
                if (time <= 999) {
                    time += 12000;
                }
            }
        }

        StringBuilder stringtime = new StringBuilder(time / 10 + "");
        for (int n = stringtime.length(); n < 4; n++) {
            stringtime.insert(0, "0");
        }

        String[] strsplit = stringtime.toString().split("");

        int minutes = (int)Math.floor(Double.parseDouble(strsplit[2] + strsplit[3])/100*60);
        String sm = minutes + "";
        if (minutes < 10) {
            sm = "0" + minutes;
        }

        if (strsplit[0].equals("0")) {
            stringtime = new StringBuilder(strsplit[1] + ":" + sm.charAt(0) + sm.charAt(1));
        }
        else {
            stringtime = new StringBuilder(strsplit[0] + strsplit[1] + ":" + sm.charAt(0) + sm.charAt(1));
        }

        return stringtime + suffix;
    }

    public static String getTimeUntil(int currentTime, int targetTime) {
        if (currentTime > targetTime) {
            currentTime = currentTime - 24000;
        }

        int totalSeconds = Math.abs(currentTime - targetTime) / 20;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        String clock = "";
        if (minutes > 0) {
            clock += minutes + "m ";
        }
        clock += seconds + "s";
        return clock;
    }
}
