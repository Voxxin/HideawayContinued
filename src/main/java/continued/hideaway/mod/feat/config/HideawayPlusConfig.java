package continued.hideaway.mod.feat.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import continued.hideaway.mod.feat.config.model.GeneralConfigModel;
import continued.hideaway.mod.feat.config.model.SoundConfigModel;
import continued.hideaway.mod.util.Constants;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class HideawayPlusConfig {
    private static final File configFile = FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID + "_" + Constants.VERSION + ".json").toFile();
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

            JsonObject general = JSONedFile.getAsJsonObject("general");
            for (GeneralConfigModel config : GeneralConfigModel.values()) {
                if (general.has(config.name)) {
                    config.value = general.get(config.name).getAsString();
                }
            }

            JsonObject sounds = JSONedFile.getAsJsonObject("sounds");
            for (SoundConfigModel config : SoundConfigModel.values()) {
                if (sounds.has(config.name)) {
                    config.value = sounds.get(config.name).getAsBoolean();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading mod config file: " + e.getMessage());
        }
    }

    public static void write() {
        JsonObject JSONedFile = new JsonObject();

        JsonObject general = new JsonObject();
        for (GeneralConfigModel config : GeneralConfigModel.values()) {
            general.addProperty(config.name, config.asString());
        }

        JsonObject sounds = new JsonObject();
        for (SoundConfigModel config : SoundConfigModel.values()) {
            sounds.addProperty(config.name, config.value);
        }

        JSONedFile.add("general", general);
        JSONedFile.add("sounds", sounds);

        try (FileWriter fileWriter = new FileWriter(configFile)) {
            gson.toJson(JSONedFile, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException("Error saving mod config file: " + e.getMessage());
        }
    }
}