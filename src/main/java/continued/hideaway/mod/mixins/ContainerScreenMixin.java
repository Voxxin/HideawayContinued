package continued.hideaway.mod.mixins;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.ext.AbstractContainerScreenAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerScreen.class)
public abstract class ContainerScreenMixin extends AbstractContainerScreen<ChestMenu> implements AbstractContainerScreenAccessor {

    public ContainerScreenMixin(ChestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void hp$slotChange(Slot slot, int slotId, int mouseButton, ClickType type) {
        slotClicked(slot, slotId, mouseButton, type);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderSlotRarity(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (HideawayPlus.connected() && HideawayPlus.config().inventoryRarities()) {
            for (int k = 0; k < (this.menu).slots.size(); ++k) {
                Slot slot = (this.menu).slots.get(k);
                TextColor itemColor = slot.getItem().getHoverName().getStyle().getColor();
                if (itemColor != null) {
                    int color = itemColor.getValue();
                    int r = (color >> 16) & 0xFF;
                    int g = (color >> 8) & 0xFF;
                    int b = color & 0xFF;

                    int itemColour = FastColor.ARGB32.color(150, r, g, b);
                    int leftX = leftPos + slot.x;
                    int leftY = topPos + slot.y;

                    guiGraphics.fill(leftX, leftY + 2, leftX + 1, leftY + 14, itemColour);
                    guiGraphics.fill(leftX + 1, leftY + 1, leftX + 2, leftY + 15, itemColour);
                    guiGraphics.fill(leftX + 2, leftY, leftX + 14, leftY + 16, itemColour);
                    guiGraphics.fill(leftX + 14, leftY + 1, leftX + 15, leftY + 15, itemColour);
                    guiGraphics.fill(leftX + 15, leftY + 2, leftX + 16, leftY + 14, itemColour);
                }
            }
        }
    }
}
