package continued.hideaway.mod.feat.keyboard;

import com.google.common.collect.Lists;
import continued.hideaway.mod.mixins.ext.KeyBindingAccessor;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import java.util.Optional;

import java.util.List;
import java.util.Map;

public class KeyBindingRegistryImpl {
    private static final List<KeyMapping> OUR_KEYBINDS = new ReferenceArrayList<>();
    private KeyBindingRegistryImpl() {
    }

    private static Map<String, Integer> getCategoryMap() {
        return KeyBindingAccessor.hp_getCategoryMap();
    }

    public static boolean addCategory(String categoryTranslationKey) {
        Map<String, Integer> map = getCategoryMap();

        if (map.containsKey(categoryTranslationKey)) {
            return false;
        }

        Optional<Integer> largest = map.values().stream().max(Integer::compareTo);
        int largestInt = largest.orElse(0);
        map.put(categoryTranslationKey, largestInt + 1);
        return true;
    }

    public static KeyMapping registerKeyBinding(KeyMapping binding) {
        if (Minecraft.getInstance().options != null) {
            throw new IllegalStateException("GameOptions has already been initialised");
        }

        for (KeyMapping existingKeyBindings : OUR_KEYBINDS) {
            if (existingKeyBindings == binding) {
                throw new IllegalArgumentException("Attempted to register a key binding twice: " + binding.getDefaultKey());
            } else if (existingKeyBindings.getDefaultKey().equals(binding.getDefaultKey())) {
                throw new IllegalArgumentException("Attempted to register two key bindings with equal ID: " + binding.getDefaultKey() + "!");
            }
        }

        // This will do nothing if the category already exists.
        addCategory(binding.getCategory());
        OUR_KEYBINDS.add(binding);
        return binding;
    }

    /**
     * Processes the keybindings array for our modded ones by first removing existing modded keybindings and readding them,
     * we can make sure that there are no duplicates this way.
     */
    public static KeyMapping[] process(KeyMapping[] keysAll) {
        List<KeyMapping> newKeysAll = Lists.newArrayList(keysAll);
        newKeysAll.removeAll(OUR_KEYBINDS);
        newKeysAll.addAll(OUR_KEYBINDS);
        return newKeysAll.toArray(new KeyMapping[0]);
    }
}
