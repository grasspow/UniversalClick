package grasspow.universal_click.neoforge;

import grasspow.universal_click.ClickConfig;
import grasspow.universal_click.ClickConfigValues;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class NeoForgeConfig implements ClickConfig {

    public static final NeoForgeConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    private static final String KEY_TICKS_STEP = "ticksStep";
    private static ModConfigSpec.IntValue TICK_STEP;

    private static final String KEY_MAX_TICKS = "maxTicksBetweenClicks";
    private static ModConfigSpec.IntValue MAX_TICKS;

    private static final String KEY_MIN_TICKS = "minTicksBetweenClicks";
    private static ModConfigSpec.IntValue MIN_TICKS;

    private NeoForgeConfig(ModConfigSpec.Builder builder) {
        TICK_STEP = builder
                .comment("When changing auto click interval, by how much should it change.")
                .defineInRange(KEY_TICKS_STEP, ClickConfigValues.DEFAULT_TICK_STEP, 1, 9999999);

        MAX_TICKS = builder
                .comment("The maximal auto click interval before looping back to the minimum.")
                .defineInRange(KEY_MAX_TICKS, ClickConfigValues.DEFAULT_MAX_TICKS, 1, 9999999);

        MIN_TICKS = builder
                .comment("The minimal auto click interval.")
                .defineInRange(KEY_MIN_TICKS, ClickConfigValues.DEFAULT_MIN_TICKS, 1, 9999999);
    }

    static {
        Pair<NeoForgeConfig, ModConfigSpec> pair =
                new ModConfigSpec.Builder().configure(NeoForgeConfig::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    @Override
    public int getTickStep() {
        return ClickConfigValues.normalize(TICK_STEP.get(), MIN_TICKS.get(), MAX_TICKS.get()).tickStep();
    }

    @Override
    public int getMaxTicks() {
        return ClickConfigValues.normalize(TICK_STEP.get(), MIN_TICKS.get(), MAX_TICKS.get()).maxTicks();
    }

    @Override
    public int getMinTicks() {
        return ClickConfigValues.normalize(TICK_STEP.get(), MIN_TICKS.get(), MAX_TICKS.get()).minTicks();
    }

}
