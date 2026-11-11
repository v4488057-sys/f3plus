package dev.coldwilder.f3plus;

import dev.coldwilder.f3plus.mixin.GameOptionsAccessor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public final class F3PlusKeybinds {
    private static KeyBinding binding;

    private F3PlusKeybinds() {
    }

    public static KeyBinding binding() {
        if (binding == null) {
            binding = new KeyBinding("key.f3plus.settings", GLFW.GLFW_KEY_F8, KeyBinding.MISC_CATEGORY);
        }
        return binding;
    }

    public static void register(GameOptions options) {
        KeyBinding key = binding();
        for (KeyBinding existing : options.allKeys) {
            if (existing == key) {
                return;
            }
        }
        KeyBinding[] keys = Arrays.copyOf(options.allKeys, options.allKeys.length + 1);
        keys[keys.length - 1] = key;
        ((GameOptionsAccessor) options).f3plus$setAllKeys(keys);
    }
}
