package continued.hideaway.mod.mixins;

import continued.hideaway.mod.util.StaticValues;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementTree;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ClientAdvancements.class)
public class AbstractClientPlayerMixin {


    @Shadow @Final private AdvancementTree tree;

    @Shadow @Final private Map<AdvancementHolder, AdvancementProgress> progress;

    @Inject(at = @At("TAIL"), method = "update")
    private void update(ClientboundUpdateAdvancementsPacket packet, CallbackInfo ci) {
        packet.getProgress().entrySet().forEach((advancement) -> {
            if (advancement.getValue().getProgressText().getString().contains("\uE256") && advancement.getValue().getProgressText().getString().contains("Added")) StaticValues.friendsCheck = false;
        });
    }
}
