package continued.hideaway.mod.mixins;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.ui.ConfigUI;
import continued.hideaway.mod.mixins.ext.GridLayoutAccessor;
import continued.hideaway.mod.util.Chars;
import continued.hideaway.mod.util.Constants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(PauseScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Component title) {
        super(title);
    }

    @Unique
    private LayoutElement returnToGameRightButton;

    @Inject(method = "createPauseMenu",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 1, shift = At.Shift.BEFORE)
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void saveLanButton(CallbackInfo ci, GridLayout gridLayout, GridLayout.RowHelper rowHelper) {
        gridLayout.visitChildren(element -> returnToGameRightButton = element);
    }

    @Inject(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout;visitWidgets(Ljava/util/function/Consumer;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void createPauseMenuButton(CallbackInfo ci, GridLayout gridLayout, GridLayout.RowHelper rowHelper, Component component) {
        int x, y;

        x = returnToGameRightButton.getX() + returnToGameRightButton.getWidth() + 4;
        y = returnToGameRightButton.getY();

        if (gridLayout != null && Constants.MOD_MENU_PRESENT) {
            final List<LayoutElement> buttons = ((GridLayoutAccessor) gridLayout).getChildren();
            if (HideawayPlus.connected()) {
                buttons.add(Button.builder(Chars.SETTINGS.getComponent(), button -> {
                            this.minecraft.setScreen(new ConfigUI(null));
                        })
                        .bounds(x, y, 20, 20)
                        .build());
            }
        }
    }
}
