package continued.hideaway.mod;

import continued.hideaway.mod.feat.api.API;
import continued.hideaway.mod.feat.config.HideawayPlusConfig;
import continued.hideaway.mod.feat.config.model.ModConfigModel;
import continued.hideaway.mod.feat.discord.DiscordManager;
import continued.hideaway.mod.feat.jukebox.Jukebox;
import continued.hideaway.mod.feat.keyboard.HPKeybinds;
import continued.hideaway.mod.feat.keyboard.KeyboardManager;
import continued.hideaway.mod.feat.lifecycle.Lifecycle;
import continued.hideaway.mod.feat.lifecycle.Task;
import continued.hideaway.mod.feat.location.Location;
import continued.hideaway.mod.feat.shop.Shop;
import continued.hideaway.mod.feat.ui.FriendsListUI;
import continued.hideaway.mod.util.Constants;
import continued.hideaway.mod.util.StaticValues;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
public class HideawayPlus implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(Constants.MOD_NAME);
    private static final ArrayList<String> debugUsers = new ArrayList<>();

    public static DiscordManager DISCORD_MANAGER;
    public static Jukebox JUKEBOX;
    public static Shop SHOP;

    private static final HideawayPlusConfig CONFIG = new HideawayPlusConfig();
    private static Location LOCATION = Location.UNKNOWN;
    private static Lifecycle LIFECYCLE;
    private static final HPKeybinds KEYBINDS = new HPKeybinds();

    @Override
    public void onInitializeClient() {
        // Initialize debug users (USE UUID)
        debugUsers.add("1228311667704193a92fc26a31a3455d"); // Bisou
        debugUsers.add("b344687bec74479a95401aa8ccb13e92"); // OliviaTheVampire
        debugUsers.add("8b484179e47e417b8ecad204aa796e79"); // Dragonostic

        Constants.MOD_MENU_PRESENT = FabricLoader.getInstance().isModLoaded("modmenu");

        // Managers and services that need to be retained after
        // initialization, and/or be accessed by other services, should
        // be initialized here.
        LIFECYCLE = new Lifecycle();

        // Managers and services that do not need to be retained after
        // initialization should be initialized here.

        new HideawayPlusConfig();

        try {
            if (ModConfigModel.DISCORD_RPC.value) DISCORD_MANAGER = new DiscordManager().start();
        } catch (Error err) {
            HideawayPlus.logger().info(err);
            return;
        }
        JUKEBOX = new Jukebox();
        SHOP = new Shop();

        // Lifecycle tasks should be initialized here.
        lifecycle()
                .add(Task.of(() -> {if (HideawayPlus.client().getWindow() != null) { new KeyboardManager();}}, 0))
                .add(Task.of(() -> {if (!HideawayPlus.connected() && API.enabled) {API.end();}}, 0))
                .add(Task.of(Location::check, 20))
                .add(Task.of(() -> {
                    try {
                        if (DiscordManager.active) DISCORD_MANAGER.update();
                        if (DiscordManager.active && !ModConfigModel.DISCORD_RPC.value) DISCORD_MANAGER.stop();
                        if (!DiscordManager.active && ModConfigModel.DISCORD_RPC.value) DISCORD_MANAGER.start();
                    } catch (Error err) {
                        HideawayPlus.logger().error(err);
                    }

                }, 10))
                .add(Task.of(() -> {
                    if (HideawayPlus.connected() && HideawayPlus.client().screen instanceof ContainerScreen) {
                        HideawayPlus.shop().tick();
                    } else if (StaticValues.shopScreenWasFilled) {
                        HideawayPlus.shop().oldShopName = null;
                        StaticValues.shopIterationNum = 0;
                        StaticValues.shopScreenWasFilled = false;
                    }
                }, 0))
                .add(Task.of(() -> {
                    if (HideawayPlus.connected()) {
                        FriendsListUI.tick();
                    }
                }, 20))
                .add(Task.of(() -> {
                    if (HideawayPlus.connected()) {
                        API.enabled = true;
                        API.tick();
                    }
                }, 0))
                .add(Task.of(() -> {
                    if (HideawayPlus.connected()) {
                        API.live();
                    }
                }, 50))
                .add(Task.of(() -> {
                    if (HideawayPlus.connected() && API.serverUnreachable) {
                        API.serverUnreachable = false;
                        API.tick();
                    }
                }, 100))
                .add(Task.of(API::modTeam, 50))
                .add(Task.of(() -> {
                    if (HideawayPlus.connected()) {
                        KEYBINDS.tick();
                    }
                }, 0));
    }

    public static boolean connected() {
        ServerData server = Minecraft.getInstance().getCurrentServer();
        if (server != null) {
            return server.ip.endsWith("playhideaway.com");
        } else return false;
    }

    public static String version() {
        return String.valueOf(
            FabricLoader.getInstance().getModContainer(Constants.MOD_ID).get().getMetadata().getVersion()
        );
    }

    public static Logger logger() { return LOGGER; }
    public static boolean debug() { return debugUsers.contains(Minecraft.getInstance().getUser().getUuid()); }
    public static Minecraft client() { return Minecraft.getInstance(); }
    public static LocalPlayer player() { return client().player; }

    public static HideawayPlusConfig config() { return CONFIG; }
    public static DiscordManager discord() { return DISCORD_MANAGER; }
    public static Jukebox jukebox() { return JUKEBOX; }
    public static Shop shop() { return SHOP; }

    public static Lifecycle lifecycle() { return LIFECYCLE; }
    public static Location location() { return LOCATION; }

    public static void setLocation(Location l) { LOCATION = l; }
}