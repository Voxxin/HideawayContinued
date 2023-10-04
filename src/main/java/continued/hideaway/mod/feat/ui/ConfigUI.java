package continued.hideaway.mod.feat.ui;

import continued.hideaway.mod.HideawayPlus;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

//public class ConfigUI extends BaseOwoScreen<FlowLayout> {

//    @Override
//    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
//        return OwoUIAdapter.create(this, Containers::verticalFlow);
//    }
//
//    @Override
//    protected void build(FlowLayout rootComponent) {
//        rootComponent.surface(Surface.flat(0x77000000))
//                .verticalAlignment(VerticalAlignment.CENTER)
//                .horizontalAlignment(HorizontalAlignment.CENTER);
//
//        FlowLayout content = Containers.verticalFlow(Sizing.content(), Sizing.content())
//                .child(Components.label(Component.translatable("text.config.hp-config.title")));
//
//        var options = new ArrayList<ConfigOption<?>>();
//
//        // General
//        options.add(new ConfigOption<>(Component.translatable("text.config.hp-config.option.hideCosmetics"), HideawayPlus.config().keys.hideCosmetics, HideawayPlus.config().hideCosmetics()));
//        options.add(new ConfigOption<>(Component.translatable("text.config.hp-config.option.discordRPC"), HideawayPlus.config().keys.discordRPC, HideawayPlus.config().discordRPC()));
//
//        options.add(new ConfigOption<>(Component.translatable("text.config.hp-config.option.autoSell"), HideawayPlus.config().keys.autoSell, HideawayPlus.config().autoSell()));
//
//        // Rooms
//        //options.add(new ConfigOption<>(Component.translatable("text.config.hp-config.option.autoEnableEditor"), HideawayPlus.config().keys.autoEnableEditor, HideawayPlus.config().autoEnableEditor()));
//
//        // Sound
//        options.add(new ConfigOption<>(Component.translatable("text.config.hp-config.option.noAmbientSounds"), HideawayPlus.config().keys.noAmbientSounds, HideawayPlus.config().noAmbientSounds()));
//        options.add(new ConfigOption<>(Component.translatable("text.config.hp-config.option.noActivitySongs"), HideawayPlus.config().keys.noActivitySongs, HideawayPlus.config().noActivitySongs()));
//
//        final var optionsScrollContainer = Containers.verticalScroll(
//                Sizing.fill(90),
//                Sizing.fill(85),
//                Components.list(
//                        options,
//                        flowLayout -> {},
//                        this::createOptionComponent,
//                        true
//                )
//        );
//
//        content.child(
//            optionsScrollContainer
//                .scrollbarThiccness(4)
//                .padding(Insets.of(1))
//        );
//        rootComponent.child(content);
//    }
//
//    private <T> FlowLayout createOptionComponent(ConfigOption<T> option) {
//        var container = Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(32));
//        container.padding(Insets.of(5));
//
//        var valueLayout = Containers.verticalFlow(Sizing.content(), Sizing.content());
//        valueLayout.verticalAlignment(VerticalAlignment.CENTER);
//        container.child(valueLayout);
//
//        if (option.value instanceof Boolean) {
//            final var valueBox = Components.checkbox(option.name);
//            valueBox.checked((Boolean) option.value);
//            valueBox.onChanged(now -> HideawayPlus.config().optionForKey(option.key).set(now));
//            valueLayout.child(valueBox.margins(Insets.horizontal(5)));
//        } // all options are boolean for now
//
//        return container;
//    }
//
//    private record ConfigOption<T>(MutableComponent name, Option.Key key, Object value) {}
//}
