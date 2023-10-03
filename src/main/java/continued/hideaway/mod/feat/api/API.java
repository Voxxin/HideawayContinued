package continued.hideaway.mod.feat.api;

import continued.hideaway.mod.HideawayPlus;

public class API {
    public static boolean serverUnreachable = false;
    public static boolean enabled = false;
    public static boolean living = false;
    public static boolean checkingUser = false;
    public static String API_KEY = "";

    public static void tick() {
        if (!enabled || serverUnreachable) return;
        if (living || !API_KEY.isEmpty()) return;
        if (checkingUser) return;
        QueryURL.asyncCreateUser(HideawayPlus.player().getStringUUID(), HideawayPlus.player().getName().getString());
        QueryURL.asyncPlayerList();
        QueryURL.asyncTeam();
    }

    public static void live() {
        if (!enabled || serverUnreachable) return;
        if (!API_KEY.isEmpty()) living = true;
        if (checkingUser) return;
        if ((!living || API_KEY.isEmpty())) QueryURL.asyncCreateUser(HideawayPlus.player().getStringUUID(), HideawayPlus.player().getName().getString());
        QueryURL.asyncLifePing(HideawayPlus.player().getStringUUID(), API_KEY);
        QueryURL.asyncPlayerList();
    }

    public static void end() {
        if (HideawayPlus.player() != null) QueryURL.asyncDestroy(HideawayPlus.player().getStringUUID(), API_KEY);
        enabled = false;
    }

    public static void modTeam() {
        QueryURL.asyncTeam();
    }
}
