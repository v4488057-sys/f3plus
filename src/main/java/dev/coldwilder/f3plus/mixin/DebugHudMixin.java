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
    private void f3plus$layout(DrawContext context, List<String> lines, boolean left, CallbackInfo ci) {
        F3PlusConfig config = F3PlusConfig.get();
        if (!config.enabled) {
            return;
        }

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        int scaledWidth = context.getScaledWindowWidth();
        DebugTextStyle.restyle(lines, renderer, scaledWidth - 6);

        if (!config.background || lines.isEmpty()) {
            return;
        }

        int width = 0;
        int last = -1;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isEmpty()) {
                continue;
            }
            width = Math.max(width, renderer.getWidth(line));
            last = i;
        }
        if (last < 0 || width <= 0) {
            return;
        }

        int top = 1;
        int bottom = Math.min(context.getScaledWindowHeight(), 10 + 9 * last);
        int x1 = left ? 1 : Math.max(0, scaledWidth - 3 - width);
        int x2 = left ? Math.min(scaledWidth, width + 3) : scaledWidth - 1;
        if (x1 < x2 && top < bottom) {
            context.fill(x1, top, x2, bottom, 0xE60D1116);
        }
    }

    @Redirect(
        method = "drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V")
    )
    private void f3plus$background(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        if (!F3PlusConfig.get().enabled) {
            context.fill(x1, y1, x2, y2, color);
        }
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
