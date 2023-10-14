package continued.hideaway.mod.feat.rendering.screen;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.util.FileParser;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class WardrobeDropSkinScreen extends Screen {
    protected WardrobeDropSkinScreen() {
        super(Component.nullToEmpty(""));
    }

    @Override
    public void onClose() {
        super.onClose();
        HideawayPlus.client().setScreen(new WardrobeSkinScreen());
    }

    @Override
    public void onFilesDrop(List<Path> files) {
        super.onFilesDrop(files);
        onSkinFilesDrop(files);
    }

    public void onSkinFilesDrop(List<Path> paths) {
        List<Path> skins = paths.stream().filter(FileParser::isValidSkin).toList();

        for (Path skin : skins) {
            String fileName = skin.getFileName().toString();

        }
    }
}
