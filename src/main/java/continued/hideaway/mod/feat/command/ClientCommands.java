package continued.hideaway.mod.feat.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ClientCommands {

    //Put ClientCommands.initCommands after you have added a command and then remove this comment
    public static void initCommands(){
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(literal("hwp")
                    .executes(context -> {
                        context.getSource().sendFeedback(Component.literal("Hideaway Plus. For more information on what features this has check the github repo. Or you can see all of the features in a YT Clip made by Blobanium by clicking on this message!").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://youtube.com/clip/UgkxQIIhS2SUsh_Cxlk_kkd3KmEFYfMdKjOd?si=CaLlFMJbEKDPmlaS"))));
                        return 0;
                    })
            );
        }));
    }
}
