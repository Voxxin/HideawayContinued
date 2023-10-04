package continued.hideaway.mod.mixins;

import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.config.ModConfigModel;
import continued.hideaway.mod.mixins.ext.SoundEventAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SoundEvent.class)
public class SoundEventMixin {
    // TODO: Implement cancelling of ambient sounds if the setting is enabled

    @Inject(at = @At("HEAD"), method = "create", cancellable = true)
    private static void newSoundEvent(ResourceLocation location, Optional<Float> range, CallbackInfoReturnable<SoundEvent> cir) {
        boolean isAmbient = location.getPath().split("\\.")[0].contains("ambient");
        boolean isActivity = location.getPath().split("\\.")[0].contains("activities");

        if (isAmbient && ModConfigModel.NO_AMBIENT_SOUNDS.value) {
            cir.setReturnValue(SoundEventAccessor.createSoundEvent(new ResourceLocation(""), 0, true));
        }

        if (isActivity && ModConfigModel.NO_ACTIVITY_SONGS.value) {
            cir.setReturnValue(SoundEventAccessor.createSoundEvent(new ResourceLocation(""), 0, true));
        }
    }
}
