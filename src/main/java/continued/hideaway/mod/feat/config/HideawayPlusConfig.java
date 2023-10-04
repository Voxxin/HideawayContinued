package continued.hideaway.mod.feat.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import continued.hideaway.mod.feat.wardrobe.WardrobeOutfit;
import continued.hideaway.mod.util.Constants;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;

import java.io.*;
import java.util.Arrays;

import static continued.hideaway.mod.util.StaticValues.wardrobeOutfits;

public class HideawayPlusConfig {
    private static final File modDir = FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID).toFile();
    private static final File configDir = new File(modDir, "configs");
    private static final File outfitsDir = new File(modDir, "outfits");

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static continued.hideaway.mod.feat.config.ModConfigModel ModConfigModel;

    public HideawayPlusConfig() {
        init();
    }

    private static void init() {
        try {
            configDir.mkdirs();
            outfitsDir.mkdirs();

            File[] configs = configDir.listFiles();
            assert configs != null;
            if (Arrays.stream(configs).anyMatch(file -> file.getName().equals("mod.json"))) {
                setupModConfig(Arrays.stream(configs).filter(file -> file.getName().equals("mod.json")).findFirst().orElse(null));
            } else updateModConfig();

            File[] outfitsFiles = outfitsDir.listFiles();
            assert outfitsFiles != null;
            for (File file : outfitsFiles) {
                if (file.isFile()) {
                    importOutfits(file);
                    if (!file.delete()) {
                        System.out.println("Failed to delete file: " + file.getName());
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void outfitFileWriter(JsonObject object, String fileName) {
        File file = new File(outfitsDir, fileName);
        try (FileWriter fileWriter = new FileWriter(file)) {
            gson.toJson(object, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException("Error saving outfits file: " + e.getMessage());
        }
    }

    private static void modConfigFileWriter(JsonObject object) {
        File file = new File(configDir, "mod.json");
        try (FileWriter fileWriter = new FileWriter(file)) {
            gson.toJson(object, fileWriter);
            System.out.println("Saved mod config file");
        } catch (IOException e) {
            throw new RuntimeException("Error saving mod config file: " + e.getMessage());
        }
    }

    private static void importOutfits(File file) throws IOException, CommandSyntaxException {
        FileReader fileReader = new FileReader(file);
        JsonObject JSONedFile = gson.fromJson(fileReader, JsonObject.class);
        fileReader.close();
        String name = JSONedFile.get("name").getAsString();
        JsonObject outfit = JSONedFile.get("outfit").getAsJsonObject();
        ItemStack head = ItemStack.EMPTY;
        ItemStack chest = ItemStack.EMPTY;
        ItemStack off_hand = ItemStack.EMPTY;

        head.setTag(TagParser.parseTag(outfit.get("head").getAsString()));
        chest.setTag(TagParser.parseTag(outfit.get("back").getAsString()));
        off_hand.setTag(TagParser.parseTag(outfit.get("off_hand").getAsString()));

        String caseName = "exist";

        if (wardrobeOutfits.stream().anyMatch(tOutfit -> tOutfit.fileName.equals(file.getName()))) return;
        wardrobeOutfits.add(new WardrobeOutfit(name, file.getName(), caseName, head, chest, off_hand));
    }

    public void updateOutfits() {
        for (WardrobeOutfit thisOutfit : wardrobeOutfits) {
            String fileName = thisOutfit.fileName != null ? thisOutfit.fileName : thisOutfit.title + ".json";

            JsonObject JSONedFile = new JsonObject();
            JsonObject outfit = new JsonObject();
            JSONedFile.addProperty("name", thisOutfit.title);
            outfit.addProperty("head", thisOutfit.head.getTag().toString());
            outfit.addProperty("back", thisOutfit.chest.getTag().toString());
            outfit.addProperty("off_hand", thisOutfit.holdable.getTag().toString());
            JSONedFile.add("outfit", outfit);

            int casesFound = 0;
            for (File outfitFiles : outfitsDir.listFiles()) {
                String name = outfitFiles.getName().replace(".json", "");
                name = name.replaceAll("\\((\\d+)\\)[^(]*$", "");
                if (name.equals(fileName.replace(".json", ""))) casesFound++;
            }

            if (casesFound > 0) fileName = fileName.replace(".json", "") + "(" + casesFound + ").json";
            outfitFileWriter(JSONedFile, fileName);
            init();
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
        System.out.println("Updating mod config file");

        JsonObject JSONedFile = new JsonObject();
        for (ModConfigModel config : ModConfigModel.values()) {
            JSONedFile.addProperty(config.name, config.value);
        }

        System.out.println(JSONedFile);

        modConfigFileWriter(JSONedFile);
        init();
    }

    public ModConfigModel[] getModConfig() {
        return ModConfigModel.values();
    }
}
