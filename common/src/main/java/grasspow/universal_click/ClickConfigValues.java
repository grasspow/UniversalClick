package grasspow.universal_click;

/**
 * Shared defaults and normalization rules for both loader configs.
 */
public final class ClickConfigValues {

    public static final int DEFAULT_TICK_STEP = 1;
    public static final int DEFAULT_MIN_TICKS = 1;
    public static final int DEFAULT_MAX_TICKS = 10;

    private ClickConfigValues() {
    }

    public static Settings normalize(int tickStep, int minTicks, int maxTicks) {
        int normalizedStep = Math.max(1, tickStep);
        int normalizedMin = Math.max(1, minTicks);
        int normalizedMax = Math.max(normalizedMin, Math.max(1, maxTicks));
        return new Settings(normalizedStep, normalizedMin, normalizedMax);
    }

    public static Settings normalize(ClickConfig config) {
        return normalize(config.getTickStep(), config.getMinTicks(), config.getMaxTicks());
    }

    public static int normalizeInterval(int interval, ClickConfig config) {
        Settings settings = normalize(config);
        return Math.max(settings.minTicks(), Math.min(interval, settings.maxTicks()));
    }

    public static int nextInterval(int current, ClickConfig config) {
        Settings settings = normalize(config);
        long next = (long) current + settings.tickStep();
        if (next < settings.minTicks() || next > settings.maxTicks()) {
            return settings.minTicks();
        }
        return (int) next;
    }

    public record Settings(int tickStep, int minTicks, int maxTicks) {
    }
}
