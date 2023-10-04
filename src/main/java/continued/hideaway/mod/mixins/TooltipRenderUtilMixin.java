package continued.hideaway.mod.mixins;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.ModConfigModel;
import continued.hideaway.mod.feat.ext.AbstractContainerScreenAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FastColor;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TooltipRenderUtil.class)
public abstract class TooltipRenderUtilMixin {

    @Inject(method = "renderTooltipBackground", at = @At("TAIL"))
    private static void renderTooltipRarity(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, CallbackInfo ci) {
        Screen screen = HideawayPlus.client().screen;

        if (screen instanceof AbstractContainerScreen && ModConfigModel.INVENTORY_RARITIES.value) {
            AbstractContainerScreenAccessor containerScreen = (AbstractContainerScreenAccessor) screen;
            Slot slot = containerScreen.hp$getHoveredSlot();

            if (containerScreen.hp$getMenu().getCarried().isEmpty() && slot != null && slot.hasItem()) {
                TextColor itemColor = slot.getItem().getHoverName().getStyle().getColor();

                if (itemColor != null) {
                    int color = itemColor.getValue();
                    int r = (color >> 16) & 0xFF;
                    int g = (color >> 8) & 0xFF;
                    int b = color & 0xFF;

                    int itemColour = FastColor.ARGB32.color(150, r, g, b);

                    guiGraphics.fill(x - 3, y - 3, x + width + 3, y - 2, z, itemColour); // Top line
                    guiGraphics.fill(x - 3, y - 2, x - 2, y + height + 3, z, itemColour); // Left line
                    guiGraphics.fill(x - 2, y + height + 3, x + width + 2, y + height + 2, z, itemColour); // Bottom line
                    guiGraphics.fill(x + width + 3, y - 2, x + width + 2, y + height + 3, z, itemColour); // Right line
                }
            }
        }
    }
}
