package continued.hideaway.mod.mixins;

import blue.endless.jankson.annotation.Nullable;
import continued.hideaway.mod.feat.ext.AbstractContainerScreenAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FastColor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin implements AbstractContainerScreenAccessor {

    @Final
    @Shadow
    protected AbstractContainerMenu menu;

    @Shadow protected Slot hoveredSlot;

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V"))
    public void renderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        TextColor itemColor = slot.getItem().getHoverName().getStyle().getColor();
        if (itemColor != null) {
            int color = itemColor.getValue();
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            int itemColour = FastColor.ARGB32.color(255, r, g, b);

            int leftX = slot.x;
            int leftY = slot.y;

            guiGraphics.fill(leftX, leftY + 2, leftX + 1, leftY + 14, 2, itemColour);
            guiGraphics.fill(leftX + 1, leftY + 1, leftX + 2, leftY + 15, 2, itemColour);
            guiGraphics.fill(leftX + 2, leftY, leftX + 14, leftY + 16, 2, itemColour);
            guiGraphics.fill(leftX + 14, leftY + 1, leftX + 15, leftY + 15, 2, itemColour);
            guiGraphics.fill(leftX + 15, leftY + 2, leftX + 16, leftY + 14, 2, itemColour);
        }
    }

    @Override
    @Nullable
    public Slot hp$getHoveredSlot() { return this.hoveredSlot; }

    @Override
    public AbstractContainerMenu hp$getMenu() { return this.menu; }
}
