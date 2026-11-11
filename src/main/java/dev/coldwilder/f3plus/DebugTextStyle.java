package dev.coldwilder.f3plus;

import net.minecraft.client.font.TextRenderer;

import java.util.ArrayList;
import java.util.List;

public final class DebugTextStyle {
    private static final String[] JUNK = {
        "SH: ",
        "CH: ",
        "SC: ",
        "Blending: ",
        "Shader: ",
        "Sound",
        "Debug charts:",
        "For help:"
    };

    private static final String[] MOD_JUNK = {
        "Sodium",
        "[Sodium",
        "TS (No",
        "N=",
        "SHR=",
        "NL=",
        "ImmediatelyFast",
        "[ImmediatelyFast",
        "ModernFix",
        "[ModernFix",
        "BadOptimizations",
        "[BadOptimizations",
        "Iris",
        "[Iris",
        "Fabric Active renderer",
        "[Fabric] Active renderer",
        "Direct Buffers",
        "Off-Heap",
        "Buffer Pool",
        "Geometry Pool",
        "Transfer Queue",
        "Chunk Builder",
        "Chunk Queues",
        "Chunks[C]"
    };

    private static final String[] COORDS = {
        "XYZ:",
        "Block:",
        "Chunk:",
        "Chunk-relative:",
        "Block position:",
        "Chunk section:",
        "Facing:",
        "Biome:",
        "Client Light:",
        "Local Difficulty:",
        "Difficulty:",
        "Targeted Block",
        "Targeted Fluid",
        "minecraft:"
    };

    private static final String[] CHUNKS = {
        "C: ",
        "Chunks:",
        "Chunk Cache",
        "Chunks[C]"
    };

    private static final String[] ENTITIES = {
        "E: ",
        "Entities:",
        "Targeted Entity"
    };

    private static final String[] PARTICLES = {
        "P: ",
        "Particles:"
    };

    private static final String[] SYSTEM = {
        "Java:",
        "CPU:",
        "Mem:",
        "Allocation:",
        "Allocated:",
        "Display:",
        "Graphics:",
        "OpenGL",
        "GL ",
        "GPU",
        "Driver",
        "Vendor",
        "LWJGL",
        "OS:",
        "Memory:"
    };

    private DebugTextStyle() {
    }

    public static void restyle(List<String> lines, TextRenderer renderer, int maxWidth) {
        F3PlusConfig config = F3PlusConfig.get();
        if (!config.enabled) {
            return;
        }

        List<String> result = new ArrayList<>(lines.size());
        boolean gap = false;

        for (String entry : lines) {
            if (entry == null || entry.isEmpty()) {
                gap = !result.isEmpty();
                continue;
            }

            String[] parts = entry.split("\n", -1);
            if (dropped(clean(parts[0]), config)) {
                continue;
            }

            for (String part : parts) {
                String line = clean(part);
                if (line.isEmpty()) {
                    gap = !result.isEmpty();
                    continue;
                }
                if (dropped(line, config) || hidden(line, config)) {
                    continue;
                }
                if (gap) {
                    result.add("");
                    gap = false;
                }
                result.add(paint(trim(line, renderer, maxWidth)));
            }
        }

        lines.clear();
        lines.addAll(result);
    }

    private static String clean(String text) {
        int mark = text.indexOf('§');
        if (mark < 0) {
            return text;
        }
        StringBuilder out = new StringBuilder(text.length());
        out.append(text, 0, mark);
        for (int i = mark; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '§') {
                i++;
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }

    private static boolean dropped(String line, F3PlusConfig config) {
        String text = line.stripLeading();
        if (matches(text, JUNK)) {
            return true;
        }
        return config.hideModLines && matches(text, MOD_JUNK);
    }

    private static boolean hidden(String line, F3PlusConfig config) {
        String text = line.stripLeading();
        if (isFpsLine(text)) {
            return false;
        }
        if (!config.showCoordinates && matches(text, COORDS)) {
            return true;
        }
        if (!config.showChunks && matches(text, CHUNKS)) {
            return true;
        }
        if (!config.showEntities && matches(text, ENTITIES)) {
            return true;
        }
        if (!config.showParticles && matches(text, PARTICLES)) {
            return true;
        }
        return !config.showSystem && matches(text, SYSTEM);
    }

    private static boolean matches(String text, String[] prefixes) {
        for (String prefix : prefixes) {
            if (text.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private static String trim(String line, TextRenderer renderer, int maxWidth) {
        if (maxWidth <= 0 || renderer.getWidth(line) <= maxWidth) {
            return line;
        }

        String suffix = "...";
        int suffixWidth = renderer.getWidth(suffix);
        int low = 0;
        int high = line.length();
        while (low < high) {
            int mid = (low + high + 1) >>> 1;
            if (renderer.getWidth(line.substring(0, mid)) + suffixWidth <= maxWidth) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return line.substring(0, low).stripTrailing() + suffix;
    }

    private static String paint(String line) {
        int start = 0;
        while (start < line.length() && line.charAt(start) == ' ') {
            start++;
        }
        return line.substring(0, start) + paintText(line.substring(start));
    }

    private static String paintText(String text) {
        if (text.startsWith("Minecraft ")) {
            return "§b" + text;
        }
        if (text.startsWith("Integrated server ")) {
            return "§8" + text;
        }
        if (isFpsLine(text)) {
            return paintFps(text);
        }

        int fc = text.indexOf(" FC: ");
        if (fc > 0) {
            return "§d" + text.substring(0, fc) + "§7 FC: §f" + text.substring(fc + 5);
        }

        if (text.startsWith("Targeted ")) {
            int sep = text.indexOf(": ");
            if (sep > 0) {
                return "§d" + text.substring(0, sep + 1) + " §f" + text.substring(sep + 2);
            }
            return "§d" + text;
        }

        int sep = text.indexOf(": ");
        if (sep > 0) {
            String label = text.substring(0, sep);
            return "§7" + label + ": " + valueColor(label) + text.substring(sep + 2);
        }

        if (Character.isDigit(text.charAt(0))) {
            return "§7" + text;
        }
        if (text.indexOf(':') > 0 && text.indexOf(' ') < 0) {
            return "§b" + text;
        }
        return "§f" + text;
    }

    private static boolean isFpsLine(String text) {
        int space = text.indexOf(' ');
        if (space <= 0 || space + 4 > text.length()) {
            return false;
        }
        if (!text.startsWith("fps", space + 1)) {
            return false;
        }
        int after = space + 4;
        if (after < text.length() && text.charAt(after) != ' ') {
            return false;
        }
        for (int i = 0; i < space; i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static String paintFps(String text) {
        String[] parts = text.split(" ");
        int fps;
        try {
            fps = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            return "§f" + text;
        }

        StringBuilder out = new StringBuilder(48);
        out.append(fpsColor(fps)).append(parts[0]).append(" §7").append(parts[1]);

        for (int i = 2; i < parts.length; i++) {
            String token = parts[i];
            if (token.isEmpty()) {
                continue;
            }
            if (token.equals("vsync")) {
                out.append(" §bvsync");
            } else if (token.endsWith(":")) {
                out.append(" §7").append(token);
            } else {
                out.append(" §f").append(token);
            }
        }
        return out.toString();
    }

    private static String fpsColor(int fps) {
        if (fps >= 60) {
            return "§a";
        }
        if (fps >= 30) {
            return "§e";
        }
        return "§c";
    }

    private static String valueColor(String label) {
        switch (label) {
            case "XYZ":
            case "Block":
            case "Chunk":
            case "Chunk-relative":
            case "Block position":
            case "Chunk section":
            case "Targeted Block":
            case "Targeted Entity":
            case "Targeted Fluid":
                return "§b";
            case "Facing":
            case "Client Light":
            case "Local Difficulty":
            case "Difficulty":
                return "§e";
            case "Biome":
                return "§a";
            default:
                return "§f";
        }
    }
}
