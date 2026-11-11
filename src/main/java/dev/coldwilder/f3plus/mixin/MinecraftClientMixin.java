package dev.coldwilder.f3plus.mixin;

import dev.coldwilder.f3plus.F3PlusConfigScreen;
import dev.coldwilder.f3plus.F3PlusKeybinds;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "tick()V", at = @At("HEAD"))
    private void f3plus$settingsKey(CallbackInfo ci) {
        if (!F3PlusKeybinds.binding().wasPressed()) {
            return;
        }
        MinecraftClient client = (MinecraftClient) (Object) this;
        if (client.currentScreen != null) {
            return;
        }
        client.setScreen(new F3PlusConfigScreen());
    }
}
