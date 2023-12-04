package continued.hideaway.mod.feat.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import continued.hideaway.mod.feat.config.model.ModConfigModel;
import continued.hideaway.mod.util.Constants;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class HideawayPlusConfig {
    private static final File configFile = FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID + ".json").toFile();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public HideawayPlusConfig() {
        try {
            if (configFile.exists()) {
                setup();
            } else write();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setup() {
        try {
            FileReader fileReader = new FileReader(configFile);
            JsonObject JSONedFile = gson.fromJson(fileReader, JsonObject.class);

            for (ModConfigModel config : ModConfigModel.values()) {
                if (JSONedFile.has(config.name)) {
                    config.value = JSONedFile.get(config.name).getAsBoolean();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading mod config file: " + e.getMessage());
        }
    }

    public static void write() {
        JsonObject JSONedFile = new JsonObject();
        for (ModConfigModel config : ModConfigModel.values()) {
            JSONedFile.addProperty(config.name, config.value);
        }

        try (FileWriter fileWriter = new FileWriter(configFile)) {
            gson.toJson(JSONedFile, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException("Error saving mod config file: " + e.getMessage());
        }
    }
}