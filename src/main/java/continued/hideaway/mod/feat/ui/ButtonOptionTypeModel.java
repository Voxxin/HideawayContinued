package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.util.Constants;
import net.minecraft.resources.ResourceLocation;

public enum ButtonOptionTypeModel {
    DROPDOWN(
            "dropdown",
            new ResourceLocation[]{new ResourceLocation(Constants.MOD_ID, "textures/gui/sprites/container/dropdown.png"),
                    new ResourceLocation(Constants.MOD_ID, "textures/gui/sprites/container/dropdown_open.png")},

            new ResourceLocation[]{new ResourceLocation(Constants.MOD_ID, "textures/gui/sprites/container/dropdown_overlay.png"),
                    new ResourceLocation(Constants.MOD_ID, "textures/gui/sprites/container/dropdown_open_overlay.png")}

    ),
    CHECKBOX(
            "checkbox",
            new ResourceLocation[]{new ResourceLocation(Constants.MOD_ID, "textures/gui/sprites/container/checkbox.png"),
                    new ResourceLocation(Constants.MOD_ID, "textures/gui/sprites/container/checkbox_selected.png")},

            new ResourceLocation[]{new ResourceLocation(Constants.MOD_ID, "textures/gui/sprites/container/checkbox_overlay.png"),
                    new ResourceLocation(Constants.MOD_ID, "textures/gui/sprites/container/checkbox_selected_overlay.png")}
    );

    public final String type;
    public final ResourceLocation[] recourseLocation;
    public final ResourceLocation[] recourseLocationOverlay;

    ButtonOptionTypeModel(String type, ResourceLocation[] recourseLocation, ResourceLocation[] recourseLocationOverlay) {
        this.type = type;
        this.recourseLocation = recourseLocation;
        this.recourseLocationOverlay = recourseLocationOverlay;
    }
}