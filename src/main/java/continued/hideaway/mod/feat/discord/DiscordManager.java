package continued.hideaway.mod.feat.discord;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.ModConfigModel;
import continued.hideaway.mod.feat.location.Location;
import continued.hideaway.mod.util.HUDUtil;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Objects;

public class DiscordManager {
    public static boolean active = false;
    private static IPCClient client;
    private static Instant start;

    public DiscordManager start() {
        if (!active && ModConfigModel.DISCORD_RPC.value) {
            HideawayPlus.logger().info("Starting Discord RPC client...");
            client = new IPCClient(1136888078510858323L);
            client.setListener(new IPCListener() {
                @Override
                public void onPacketSent(IPCClient client, Packet packet) {

                }

                @Override
                public void onPacketReceived(IPCClient client, Packet packet) {

                }

                @Override
                public void onActivityJoin(IPCClient client, String secret) {

                }

                @Override
                public void onActivitySpectate(IPCClient client, String secret) {

                }

                @Override
                public void onActivityJoinRequest(IPCClient client, String secret, User user) {

                }

                @Override
                public void onReady(IPCClient client) {
                    HideawayPlus.logger().info("Discord RPC client connected!");
                    active = true;
                    start = Instant.now();
                    update();
                }

                @Override
                public void onClose(IPCClient client, JsonObject json) {
                    active = false;
                }

                @Override
                public void onDisconnect(IPCClient client, Throwable t) {
                    active = false;
                }
            });

            try {
                client.connect();
            } catch (Exception ignored) {}
        }
        return this;
    }

    public void update() {
        if (active && ModConfigModel.DISCORD_RPC.value) {
            Location loc = HideawayPlus.location();
            RichPresence.Builder builder = new RichPresence.Builder();

            JsonArray buttonsArray = new JsonArray();

            JsonObject discordLinkButton = new JsonObject();
            discordLinkButton.addProperty("label", "Discord");
            discordLinkButton.addProperty("url", "https://discord.gg/UktczTSdB4");

            JsonObject githubLinkButton = new JsonObject();
            githubLinkButton.addProperty("label", "GitHub");
            githubLinkButton.addProperty("url", "https://github.com/Voxxin/HideawayContinued");


            buttonsArray.add(discordLinkButton);
            buttonsArray.add(githubLinkButton);

            builder.setState(loc.description)
                    .setDetails(loc.name.contains("<player>") ? loc.name.replace("<player>", Objects.requireNonNull(HUDUtil.getCurrentRoomName())) : loc.name)
                    .setStartTimestamp(Instant.ofEpochSecond(start.toEpochMilli()).atOffset(ZoneOffset.UTC).toEpochSecond())
                    .setLargeImage(loc.largeIcon.key(), "HideawayPlus v" + HideawayPlus.version())
                    .setSmallImage(loc.smallIcon.key(), "Nothing to see here...")
                    .setButtons(buttonsArray);
            client.sendRichPresence(builder.build());
        } else start();
    }

    public void stop() {
            client.sendRichPresence(null);
            active = false;
    }
}