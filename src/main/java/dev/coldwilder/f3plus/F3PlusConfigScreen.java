package dev.coldwilder.f3plus;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;

public final class F3PlusConfigScreen extends Screen {
    private static final int WIDTH = 150;
    private static final int HEIGHT = 20;
    private static final int STEP = 24;

    private final Screen parent;

    public F3PlusConfigScreen() {
        this(null);
    }

    public F3PlusConfigScreen(Screen parent) {
        super(Text.literal("F3 Plus"));
        this.parent = parent;
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    protected void init() {
        F3PlusConfig config = F3PlusConfig.get();
        int left = this.width / 2 - WIDTH - 2;
        int right = this.width / 2 + 2;
        int top = this.height / 2 - 62;

        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.enabled)
            .build(left, top, WIDTH, HEIGHT, Text.literal("Mod"), (button, value) -> {
                config.enabled = value;
                config.save();
            }));
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.background)
            .build(left, top + STEP, WIDTH, HEIGHT, Text.literal("Background"), (button, value) -> {
                config.background = value;
                config.save();
            }));
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.showCoordinates)
            .build(left, top + STEP * 2, WIDTH, HEIGHT, Text.literal("Coordinates"), (button, value) -> {
                config.showCoordinates = value;
                config.save();
            }));
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.showChunks)
            .build(left, top + STEP * 3, WIDTH, HEIGHT, Text.literal("Chunks"), (button, value) -> {
                config.showChunks = value;
                config.save();
            }));

        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.hideModLines)
            .build(right, top, WIDTH, HEIGHT, Text.literal("Hide mod lines"), (button, value) -> {
                config.hideModLines = value;
                config.save();
            }));
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.showEntities)
            .build(right, top + STEP, WIDTH, HEIGHT, Text.literal("Entities"), (button, value) -> {
                config.showEntities = value;
                config.save();
            }));
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.showParticles)
            .build(right, top + STEP * 2, WIDTH, HEIGHT, Text.literal("Particles"), (button, value) -> {
                config.showParticles = value;
                config.save();
            }));
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.showSystem)
            .build(right, top + STEP * 3, WIDTH, HEIGHT, Text.literal("System"), (button, value) -> {
                config.showSystem = value;
                config.save();
            }));
        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(config.showBlockTags)
            .build(left, top + STEP * 4, WIDTH, HEIGHT, Text.literal("Block tags"), (button, value) -> {
                config.showBlockTags = value;
                config.save();
            }));

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> this.close())
            .dimensions(this.width / 2 - 100, top + STEP * 5 + 8, 200, HEIGHT)
            .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 24, 0xFFFFFF);
        Text key = Text.literal("Key: ").append(F3PlusKeybinds.binding().getBoundKeyLocalizedText());
        context.drawCenteredTextWithShadow(this.textRenderer, key, this.width / 2, 36, 0xA0A0A0);
    }
}
