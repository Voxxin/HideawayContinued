package continued.hideaway.mod.feat.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import continued.hideaway.mod.feat.config.model.ModConfigModel;
import continued.hideaway.mod.util.Constants;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.*;

public class HideawayPlusConfig {
    private static final File modDir = FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID).toFile();
    private static final File configDir = new File(modDir, "configs");

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public HideawayPlusConfig() {
        init();
    }

    public static void init() {
        try {
            if (configDir.mkdirs()) {
                File[] configs = configDir.listFiles();
                assert configs != null;
                if (Arrays.stream(configs).anyMatch(file -> file.getName().equals("mod.json"))) {
                    setupModConfig(Arrays.stream(configs).filter(file -> file.getName().equals("mod.json")).findFirst().orElse(null));
                } else updateModConfig();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void modConfigFileWriter(JsonObject object) {
        File file = new File(configDir, "mod.json");
        try (FileWriter fileWriter = new FileWriter(file)) {
            gson.toJson(object, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException("Error saving mod config file: " + e.getMessage());
        }
    }

    private static void setupModConfig(File file) {
        try {
            JsonObject JSONedFile = new JsonObject();
            if (file != null) {
                FileReader fileReader = new FileReader(file);
                JSONedFile = gson.fromJson(fileReader, JsonObject.class);
            }

            for (ModConfigModel config : ModConfigModel.values()) {
                if (JSONedFile.has(config.name)) {
                    config.value = JSONedFile.get(config.name).getAsBoolean();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading mod config file: " + e.getMessage());
        }
    }

    public static void updateModConfig() {
        JsonObject JSONedFile = new JsonObject();
        for (ModConfigModel config : ModConfigModel.values()) {
            JSONedFile.addProperty(config.name, config.value);
        }
        modConfigFileWriter(JSONedFile);
        init();
    }
}