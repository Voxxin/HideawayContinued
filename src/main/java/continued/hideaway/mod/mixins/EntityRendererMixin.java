package continued.hideaway.mod.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import continued.hideaway.mod.HideawayPlus;
import continued.hideaway.mod.util.Chars;
import continued.hideaway.mod.util.DisplayNameUtil;
import continued.hideaway.mod.util.StaticValues;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin <T extends Entity>{

    @Shadow protected abstract boolean shouldShowName(T entity);

    @Shadow protected abstract void renderNameTag(T entity, Component displayName, PoseStack poseStack, MultiBufferSource buffer, int packedLight);

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (this.shouldShowName(entity) && HideawayPlus.connected() && entity instanceof Player) {
            String playerName = DisplayNameUtil.ignFromDisplayName(entity.getDisplayName().getString());
            String playerID = DisplayNameUtil.modPlayerID(playerName);

            MutableComponent newName = MutableComponent.create(ComponentContents.EMPTY);
            newName.append(entity.getDisplayName().getString());

            if (StaticValues.friendsUsernames.contains(playerName)) Chars.FRIEND.addBadge(newName);

            if (StaticValues.devs.contains(playerID)) Chars.DEV.addBadge(newName);
            else if (StaticValues.teamMembers.contains(playerID)) Chars.TEAM.addBadge(newName);
            else if (StaticValues.translators.contains(playerID)) Chars.TRANSLATOR.addBadge(newName);
            else if (StaticValues.users.containsKey(playerID)) Chars.USER.addBadge(newName);

            if (newName.getString().equals(playerName)) newName = (MutableComponent) entity.getDisplayName();

            this.renderNameTag(entity, newName, poseStack, buffer, packedLight);
        }
        ci.cancel();
    }
}
