package dev.coldwilder.f3plus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class F3PlusConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static F3PlusConfig current;

    public boolean enabled = true;
    public boolean background = true;
    public boolean hideModLines = true;
    public boolean showCoordinates = true;
    public boolean showChunks = true;
    public boolean showEntities = true;
    public boolean showParticles = true;
    public boolean showSystem = true;
    public boolean showBlockTags = false;

    public F3PlusConfig() {
    }

    public static F3PlusConfig get() {
        if (current == null) {
            load();
        }
        return current;
    }

    public static void load() {
        Path path = path();
        F3PlusConfig loaded = new F3PlusConfig();
        if (Files.isRegularFile(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                F3PlusConfig fromFile = GSON.fromJson(reader, F3PlusConfig.class);
                if (fromFile != null) {
                    loaded = fromFile;
                }
            } catch (IOException | RuntimeException ignored) {
            }
        }
        current = loaded;
    }

    public void save() {
        Path path = path();
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException ignored) {
        }
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("f3plus.json");
    }
}
