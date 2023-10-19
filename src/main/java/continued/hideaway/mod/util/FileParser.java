package continued.hideaway.mod.util;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class FileParser {
    public static boolean isValidSkin(Path filePath) {
        try {
            NativeImage nativeImage = NativeImage.read((InputStream) filePath);
            int width = nativeImage.getWidth();
            int height = nativeImage.getHeight();

            if (width == 64 && height == 64) {
                if (nativeImage.format() == NativeImage.Format.RGBA) {
                    return true;
                }
            }

            nativeImage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
