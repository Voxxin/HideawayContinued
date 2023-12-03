package continued.hideaway.mod.mixins;

import net.minecraft.client.gui.components.LogoRenderer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LogoRenderer.class)
public class LogoRendererMixin {
    @ModifyConstant(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", constant = @Constant(intValue = 44, ordinal = 0))
    private int logoBlitHeight(int h) {
        return 64;
    }
}
