package dev.coldwilder.f3plus.mixin;

import dev.coldwilder.f3plus.DebugTextStyle;
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
        DebugTextStyle.restyle(lines);
    }

    @Redirect(
        method = "drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V")
    )
    private void f3plus$background(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        context.fill(x1, y1, x2, y2, 0x8C0D1116);
    }

    @Redirect(
        method = "drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"
        )
    )
    private int f3plus$text(DrawContext context, TextRenderer renderer, String text, int x, int y, int color, boolean shadow) {
        return context.drawText(renderer, text, x, y, 0xFFFFFFFF, false);
    }
}
