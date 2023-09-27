package continued.hideaway.mod.mixins;

import com.mojang.authlib.GameProfile;
import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class PlayerInfoMixin {
    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    public void skinLookUp(CallbackInfoReturnable<PlayerSkin> cir) {
        if (HideawayPlus.client().player != null && !StaticValues.wardrobeEntity.isEmpty()) {
            PlayerInfo clientPlayer = Minecraft.getInstance().getConnection().getPlayerInfo(HideawayPlus.player().getUUID());

            if (StaticValues.wardrobeEntity.contains(((AbstractClientPlayer) (Object) this).getStringUUID())) cir.setReturnValue(clientPlayer.getSkin());
        }
    }
}
