package dev.coldwilder.f3plus.mixin;

import dev.coldwilder.f3plus.F3PlusKeybinds;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Inject(method = "load()V", at = @At("HEAD"))
    private void f3plus$registerKey(CallbackInfo ci) {
        F3PlusKeybinds.register((GameOptions) (Object) this);
    }
}
