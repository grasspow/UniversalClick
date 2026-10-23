package grasspow.universal_click.neoforge;

import grasspow.universal_click.ClickConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class NeoForgeConfig implements ClickConfig {

    public static final NeoForgeConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    private static final String KEY_TICKS_STEP = "ticksStep";
    private static final int DEF_TICKS_STEP = 1;
    private static ModConfigSpec.IntValue TICK_STEP;

    private static final String KEY_MAX_TICKS = "maxTicksBetweenClicks";
    private static final int DEF_MAX_TICKS = 10;
    private static ModConfigSpec.IntValue MAX_TICKS;

    private static final String KEY_MIN_TICKS = "minTicksBetweenClicks";
    private static final int DEF_MIN_TICKS = 1;
    private static ModConfigSpec.IntValue MIN_TICKS;

    private NeoForgeConfig(ModConfigSpec.Builder builder) {
        TICK_STEP = builder.defineInRange(KEY_TICKS_STEP, DEF_TICKS_STEP, 1, 9999999);

        MAX_TICKS = builder.defineInRange(KEY_MAX_TICKS, DEF_MAX_TICKS, 1, 9999999);

        MIN_TICKS = builder.defineInRange(KEY_MIN_TICKS, DEF_MIN_TICKS, 1, 9999999);
    }

    static {
        Pair<NeoForgeConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(NeoForgeConfig::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    @Override
    public int getTickStep() {
        return TICK_STEP.get();
    }

    @Override
    public int getMaxTicks() {
        return MAX_TICKS.get();
    }

    @Override
    public int getMinTicks() {
        return MIN_TICKS.get();
    }

    public static void reload() {
        // No-op, ModConfigSpec handles caching automatically
    }

}
