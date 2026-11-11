package dev.coldwilder.f3plus.mixin;

import dev.coldwilder.f3plus.F3PlusConfigScreen;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey(JIIII)V", at = @At("HEAD"))
    private void f3plus$settings(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (action != GLFW.GLFW_PRESS || key != GLFW.GLFW_KEY_F8) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getWindow().getHandle() != window || client.currentScreen != null) {
            return;
        }
        client.setScreen(new F3PlusConfigScreen());
    }
}
