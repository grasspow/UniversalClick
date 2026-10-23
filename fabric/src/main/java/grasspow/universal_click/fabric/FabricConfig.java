package grasspow.universal_click.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import grasspow.universal_click.ClickConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FabricConfig implements ClickConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final int DEFAULT_TICK_STEP = 1;
    private static final int DEFAULT_MAX_TICKS = 10;
    private static final int DEFAULT_MIN_TICKS = 1;

    private int tickStep = DEFAULT_TICK_STEP;
    private int maxTicks = DEFAULT_MAX_TICKS;
    private int minTicks = DEFAULT_MIN_TICKS;

    private final Path configPath;

    public FabricConfig() {
        configPath = FabricLoader.getInstance().getConfigDir().resolve("universal_click.json");
        load();
    }

    private void load() {
        if (Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                if (json != null) {
                    tickStep = getInt(json, "ticksStep", DEFAULT_TICK_STEP);
                    maxTicks = getInt(json, "maxTicksBetweenClicks", DEFAULT_MAX_TICKS);
                    minTicks = getInt(json, "minTicksBetweenClicks", DEFAULT_MIN_TICKS);
                }
            } catch (IOException e) {
                // Use defaults
            }
        } else {
            save();
        }
    }

    public void save() {
        try {
            Files.createDirectories(configPath.getParent());
            try (Writer writer = Files.newBufferedWriter(configPath)) {
                JsonObject json = new JsonObject();
                json.addProperty("ticksStep", tickStep);
                json.addProperty("maxTicksBetweenClicks", maxTicks);
                json.addProperty("minTicksBetweenClicks", minTicks);
                GSON.toJson(json, writer);
            }
        } catch (IOException e) {
            // Fail silently
        }
    }

    @Override
    public int getTickStep() {
        return tickStep;
    }

    @Override
    public int getMaxTicks() {
        return maxTicks;
    }

    @Override
    public int getMinTicks() {
        return minTicks;
    }

    private static int getInt(JsonObject json, String key, int defaultValue) {
        if (json.has(key)) {
            return json.get(key).getAsInt();
        }
        return defaultValue;
    }

}
