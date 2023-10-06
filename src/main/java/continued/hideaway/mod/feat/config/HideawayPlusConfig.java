package continued.hideaway.mod.feat.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import continued.hideaway.mod.feat.config.model.ModConfigModel;
import continued.hideaway.mod.feat.wardrobe.WardrobeOutfit;
import continued.hideaway.mod.util.Constants;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static continued.hideaway.mod.util.StaticValues.wardrobeOutfits;

public class HideawayPlusConfig {
    private static final File modDir = FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID).toFile();
    private static final File configDir = new File(modDir, "configs");
    private static final File outfitsDir = new File(modDir, "outfits");

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static continued.hideaway.mod.feat.config.model.ModConfigModel ModConfigModel;

    public HideawayPlusConfig() {
        init();
    }

    public static void init() {
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
            wardrobeOutfits.clear();
            for (File file : outfitsFiles) {
                if (file.isFile()) {
                    importOutfits(file);
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
        JsonObject headO = outfit.get("head").getAsJsonObject();
        JsonObject chestO = outfit.get("chest").getAsJsonObject();
        JsonObject off_handO = outfit.get("off_hand").getAsJsonObject();

        ItemStack head = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(headO.get("registry").getAsString())).getDefaultInstance();
        ItemStack chest = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(chestO.get("registry").getAsString())).getDefaultInstance();
        ItemStack off_hand = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(off_handO.get("registry").getAsString())).getDefaultInstance();
        if (!headO.get("nbt").getAsString().isEmpty()) head.setTag(TagParser.parseTag(headO.get("nbt").getAsString()));
        if (!chestO.get("nbt").getAsString().isEmpty()) chest.setTag(TagParser.parseTag(chestO.get("nbt").getAsString()));
        if (!off_handO.get("nbt").getAsString().isEmpty()) off_hand.setTag(TagParser.parseTag(off_handO.get("nbt").getAsString()));

//        if (wardrobeOutfits.stream().anyMatch(tOutfit -> tOutfit.fileName.equals(file.getName()))) return;
        wardrobeOutfits.add(new WardrobeOutfit(name, file.getName(), "", head, chest, off_hand));
    }

    public static void updateOutfits() {
        List<WardrobeOutfit> outfitsCopy = new ArrayList<>(wardrobeOutfits);

        for (WardrobeOutfit thisOutfit : outfitsCopy) {
            if ((thisOutfit.caseName.equals("remove") || thisOutfit.caseName.equals("delete")) && !thisOutfit.fileName.isEmpty() && outfitsDir.listFiles() != null) {
                for (File outfitFiles : outfitsDir.listFiles()) {
                    if (outfitFiles.getName().equals(thisOutfit.fileName)) {
                        outfitFiles.delete();
                        init();
                        break;
                    }
                }
                continue;
            }

            String fileName = !thisOutfit.fileName.isEmpty() ? thisOutfit.fileName : thisOutfit.title.toLowerCase() + ".json";

            JsonObject JSONedFile = new JsonObject();
            JsonObject outfit = new JsonObject();
            JSONedFile.addProperty("name", thisOutfit.title);
            JsonObject head = new JsonObject();
            JsonObject chest = new JsonObject();
            JsonObject off_hand = new JsonObject();
            head.addProperty("registry", BuiltInRegistries.ITEM.getKey(thisOutfit.head.getItem()).toString());
            head.addProperty("nbt", thisOutfit.head.getTag() != null ? thisOutfit.head.getTag().getAsString() : "");

            chest.addProperty("registry", BuiltInRegistries.ITEM.getKey(thisOutfit.chest.getItem()).toString());
            chest.addProperty("nbt", thisOutfit.chest.getTag() != null ? thisOutfit.chest.getTag().getAsString() : "");

            off_hand.addProperty("registry", BuiltInRegistries.ITEM.getKey(thisOutfit.holdable.getItem()).toString());
            off_hand.addProperty("nbt", thisOutfit.holdable.getTag() != null ? thisOutfit.holdable.getTag().getAsString() : "");

            outfit.add("head", head);
            outfit.add("chest", chest);
            outfit.add("off_hand", off_hand);

            JSONedFile.add("outfit", outfit);

            if (head.get("nbt").getAsString().isEmpty() && chest.get("nbt").getAsString().isEmpty() && off_hand.get("nbt").getAsString().isEmpty()) continue;

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
        JsonObject JSONedFile = new JsonObject();
        for (ModConfigModel config : ModConfigModel.values()) {
            JSONedFile.addProperty(config.name, config.value);
        }

        modConfigFileWriter(JSONedFile);
        init();
    }
}