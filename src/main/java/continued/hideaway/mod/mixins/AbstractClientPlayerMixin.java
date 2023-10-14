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
import net.minecraft.world.phys.Vec3;
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

    @Unique
    private Vec3 oldPos = new Vec3(0, 0, 0);
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {

        if (HideawayPlus.client().player != null && Wardrobe.wardrobePlayer != null && Wardrobe.wardrobePlayer.getStringUUID().equals(player.getStringUUID())) {
            playerInfo = HideawayPlus.player().connection.getPlayerInfo(HideawayPlus.player().getGameProfile().getId());
        }


        Vec3 emptyPos = new Vec3(0, 0, 0);
        Vec3 playerPos = new Vec3(player.getX(), player.getY(), player.getZ());
        if (Wardrobe.wardrobePlayer != null && Wardrobe.wardrobePlayer.getStringUUID().equals(player.getStringUUID()) && (emptyPos.equals(oldPos) || !playerPos.equals(oldPos))) {
            oldPos = playerPos;
            Wardrobe.wardrobePlayer.setPos(oldPos.x, oldPos.y, oldPos.z);
        }


        if (StaticValues.wardrobeEntity.contains(player.getStringUUID()) && Wardrobe.wardrobePlayer == null) {
            assert HideawayPlus.client().level != null;

            AbstractClientPlayer fakePlayer = createFakePlayer();
            fakePlayer.setPos(player.getX(), HideawayPlus.player().getY() + 1, player.getZ());

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = player.getItemBySlot(slot);
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
