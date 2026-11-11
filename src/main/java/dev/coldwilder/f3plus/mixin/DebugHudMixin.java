package dev.coldwilder.f3plus.mixin;

import dev.coldwilder.f3plus.DebugTextStyle;
import dev.coldwilder.f3plus.F3PlusConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugHud.class)
public abstract class DebugHudMixin {
    @Inject(
        method = "drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V",
        at = @At("HEAD")
    )
    private void f3plus$restyle(DrawContext context, List<String> lines, boolean left, CallbackInfo ci) {
        if (!F3PlusConfig.get().enabled) {
            return;
        }
        DebugTextStyle.restyle(lines, MinecraftClient.getInstance().textRenderer, context.getScaledWindowWidth() - 6);
    }

    @Redirect(
        method = "drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V")
    )
    private void f3plus$background(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        F3PlusConfig config = F3PlusConfig.get();
        if (config.enabled && !config.background) {
            return;
        }
        context.fill(x1, y1, x2, y2, color);
    }

    @Redirect(
        method = "drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"
        )
    )
    private int f3plus$text(DrawContext context, TextRenderer renderer, String text, int x, int y, int color, boolean shadow) {
        if (!F3PlusConfig.get().enabled) {
            return context.drawText(renderer, text, x, y, color, shadow);
        }
        return context.drawText(renderer, text, x, y, 0xFFFFFFFF, false);
    }
}
