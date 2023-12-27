package continued.hideaway.mod.mixins;

import continued.hideaway.mod.util.StaticValues;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementTree;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientAdvancements.class)
public class ClientAdvancementsMixin {
    @Shadow @Final private AdvancementTree tree;

    @Inject(at = @At("TAIL"), method = "update")
    private void update(ClientboundUpdateAdvancementsPacket packet, CallbackInfo ci) {
        this.tree.nodes().forEach((advancement) -> {
            Advancement advancement1 = advancement.advancement();
            if (advancement1.display().isEmpty()) return;
            if (advancement1.display().get().getTitle().getString().contains("\uE256") && advancement1.display().get().getTitle().getString().contains("Added")) StaticValues.friendsCheck = false;
        });
    }
}
