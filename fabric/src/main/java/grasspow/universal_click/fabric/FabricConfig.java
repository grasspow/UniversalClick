package grasspow.universal_click.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import grasspow.universal_click.ClickConfig;
import grasspow.universal_click.ClickConfigValues;
import grasspow.universal_click.UniversalClick;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FabricConfig implements ClickConfig {

    public static final String FILE_NAME = "universal_click.json";

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private int tickStep = ClickConfigValues.DEFAULT_TICK_STEP;
    private int maxTicks = ClickConfigValues.DEFAULT_MAX_TICKS;
    private int minTicks = ClickConfigValues.DEFAULT_MIN_TICKS;

    private final Path configPath;

    public FabricConfig() {
        this(FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME));
    }

    FabricConfig(Path configPath) {
        this.configPath = configPath;
        load();
    }

    private void load() {
        if (!Files.exists(configPath)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(configPath)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            if (json != null) {
                tickStep = getInt(json, "ticksStep", ClickConfigValues.DEFAULT_TICK_STEP);
                maxTicks = getInt(json, "maxTicksBetweenClicks", ClickConfigValues.DEFAULT_MAX_TICKS);
                minTicks = getInt(json, "minTicksBetweenClicks", ClickConfigValues.DEFAULT_MIN_TICKS);
            }
        } catch (Exception e) {
            UniversalClick.LOG.warn("[UniversalClick] Failed to read config {}, using defaults", configPath, e);
        }

        ClickConfigValues.Settings settings = ClickConfigValues.normalize(tickStep, minTicks, maxTicks);
        tickStep = settings.tickStep();
        minTicks = settings.minTicks();
        maxTicks = settings.maxTicks();
    }

    public void apply(int tickStep, int minTicks, int maxTicks) {
        ClickConfigValues.Settings settings = ClickConfigValues.normalize(tickStep, minTicks, maxTicks);
        this.tickStep = settings.tickStep();
        this.minTicks = settings.minTicks();
        this.maxTicks = settings.maxTicks();
        save();
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
        } catch (Exception e) {
            UniversalClick.LOG.warn("[UniversalClick] Failed to write config {}", configPath, e);
        }
    }

    public Path getConfigPath() {
        return configPath;
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
        try {
            JsonElement element = json.get(key);
            if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsInt();
            }
        } catch (RuntimeException e) {
            UniversalClick.LOG.warn("[UniversalClick] Invalid config value for {}", key, e);
        }
        return defaultValue;
    }
}
