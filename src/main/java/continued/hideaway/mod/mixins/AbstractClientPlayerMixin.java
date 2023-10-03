package continued.hideaway.mod.mixins;

import com.mojang.authlib.GameProfile;
import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.feat.wardrobe.Wardrobe;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.function.BiConsumer;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {
    @Shadow private @Nullable PlayerInfo playerInfo;
    @Unique
    private final AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {

        if (HideawayPlus.client().player != null && Wardrobe.wardrobePlayer != null && Wardrobe.wardrobePlayer.getStringUUID().equals(player.getStringUUID())) {
            playerInfo = HideawayPlus.player().connection.getPlayerInfo(HideawayPlus.player().getGameProfile().getId());
        }


        if (StaticValues.wardrobeEntity.contains(player.getStringUUID()) && Wardrobe.wardrobePlayer == null) {
            assert HideawayPlus.client().level != null;

            AbstractClientPlayer fakePlayer = createFakePlayer();
            fakePlayer.setPos(player.getX(), HideawayPlus.player().getY() + 1, player.getZ());

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = player.getItemBySlot(slot);
                System.out.println(slot + " " + stack);
                fakePlayer.setItemSlot(slot, stack);
            }

            player.discard();
            player.kill();
            player.remove(Entity.RemovalReason.DISCARDED);

            HideawayPlus.client().level.addPlayer(fakePlayer.getId(), fakePlayer);
            Wardrobe.wardrobePlayer = fakePlayer;
        }
    }

    @Unique
    private AbstractClientPlayer createFakePlayer() {
        GameProfile gameProfile = new GameProfile(UUID.fromString("0000000f-00d0-0000-0000-000000e00e00"), "");
        return new AbstractClientPlayer(HideawayPlus.client().level != null ? HideawayPlus.client().level : null, gameProfile) {
        };
    }
}
