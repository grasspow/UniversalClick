package grasspow.universal_click.fabric;

import grasspow.universal_click.ClickHandler;
import grasspow.universal_click.ClickHandlerAccess;
import grasspow.universal_click.VersionCompat;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class UniversalClickConfigScreen extends Screen {

    private static final int LABEL_COLOR = 0xFFFFFF;
    private static final int ERROR_COLOR = 0xFF5555;

    private final Screen parent;
    private final FabricConfig config;

    private EditBox tickStepField;
    private EditBox minTicksField;
    private EditBox maxTicksField;
    private Component error = Component.empty();

    public UniversalClickConfigScreen(Screen parent) {
        super(Component.translatable("universal_click.configuration.title"));
        this.parent = parent;

        ClickHandler handler = ClickHandlerAccess.get();
        if (handler != null && handler.getConfig() instanceof FabricConfig fabricConfig) {
            this.config = fabricConfig;
        } else {
            this.config = new FabricConfig();
        }
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int fieldX = centerX - 100;

        tickStepField = addRenderableWidget(new EditBox(font, fieldX, 62, 200, 20,
                Component.translatable("universal_click.configuration.ticksStep")));
        tickStepField.setMaxLength(7);
        tickStepField.setValue(Integer.toString(config.getTickStep()));

        minTicksField = addRenderableWidget(new EditBox(font, fieldX, 107, 200, 20,
                Component.translatable("universal_click.configuration.minTicksBetweenClicks")));
        minTicksField.setMaxLength(7);
        minTicksField.setValue(Integer.toString(config.getMinTicks()));

        maxTicksField = addRenderableWidget(new EditBox(font, fieldX, 152, 200, 20,
                Component.translatable("universal_click.configuration.maxTicksBetweenClicks")));
        maxTicksField.setMaxLength(7);
        maxTicksField.setValue(Integer.toString(config.getMaxTicks()));

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> save())
                .bounds(centerX - 102, height - 40, 98, 20)
                .build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> onClose())
                .bounds(centerX + 4, height - 40, 98, 20)
                .build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        int centerX = width / 2;
        int labelX = centerX - 100;

        graphics.centeredText(font, title, centerX, 30, LABEL_COLOR);
        graphics.text(font, Component.translatable("universal_click.configuration.ticksStep"), labelX, 50, LABEL_COLOR);
        graphics.text(font, Component.translatable("universal_click.configuration.minTicksBetweenClicks"), labelX, 95, LABEL_COLOR);
        graphics.text(font, Component.translatable("universal_click.configuration.maxTicksBetweenClicks"), labelX, 140, LABEL_COLOR);

        if (!error.getString().isEmpty()) {
            graphics.centeredText(font, error, centerX, 190, ERROR_COLOR);
        }
    }

    private void save() {
        int tickStep;
        int minTicks;
        int maxTicks;
        try {
            tickStep = Integer.parseInt(tickStepField.getValue().trim());
            minTicks = Integer.parseInt(minTicksField.getValue().trim());
            maxTicks = Integer.parseInt(maxTicksField.getValue().trim());
        } catch (NumberFormatException e) {
            error = Component.translatable("config.universal_click.invalid_number");
            return;
        }

        if (tickStep < 1 || minTicks < 1 || maxTicks < 1 || minTicks > maxTicks) {
            error = Component.translatable("config.universal_click.invalid_range");
            return;
        }

        config.apply(tickStep, minTicks, maxTicks);
        ClickHandler handler = ClickHandlerAccess.get();
        if (handler != null) {
            handler.onConfigChanged();
        }
        VersionCompat.setScreen(minecraft, parent);
    }

    @Override
    public void onClose() {
        VersionCompat.setScreen(minecraft, parent);
    }
}
