package continued.hideaway.mod.mixins.ext;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientPacketListener.class)
public interface ClientPacketListenerAccessor {
    @Invoker("sendCommand")
    void hp$sendCommand(String command);
}
