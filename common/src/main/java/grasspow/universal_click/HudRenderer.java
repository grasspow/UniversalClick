package grasspow.universal_click;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class HudRenderer {

    private static final int X = 6;
    private static final int Y = 6;
    private static final int LINE_HEIGHT = 10;
    private static final int COLOR = 0xFFFF0000;

    private final Minecraft minecraft;

    private ClickHandler.HudState cachedState;
    private String cachedLanguage;
    private List<Component> cachedLines = List.of();

    public HudRenderer(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void render(GuiGraphicsExtractor graphics) {
        ClickHandler handler = ClickHandlerAccess.get();
        if (handler == null) {
            return;
        }

        ClickHandler.HudState state = handler.getHudState();
        String language = minecraft.getLanguageManager().getSelected();
        if (!state.equals(cachedState) || !language.equals(cachedLanguage)) {
            cachedState = state;
            cachedLanguage = language;
            cachedLines = buildLines(state);
        }

        for (int i = 0; i < cachedLines.size(); ++i) {
            graphics.text(minecraft.font, cachedLines.get(i), X, Y + LINE_HEIGHT * i, COLOR);
        }
    }

    private static List<Component> buildLines(ClickHandler.HudState state) {
        List<Component> lines = new ArrayList<>(5);

        if (state.autoLeft()) {
            lines.add(line("hud.universal_click.auto_left"));
        }
        if (state.autoRight()) {
            lines.add(line("hud.universal_click.auto_right"));
        }
        if (state.smartAttack()) {
            lines.add(line("hud.universal_click.smart_attack"));
        }
        if (state.holdLeft()) {
            lines.add(line("hud.universal_click.hold_left"));
        }
        if (state.holdRight()) {
            lines.add(line("hud.universal_click.hold_right"));
        }

        return List.copyOf(lines);
    }

    private static Component line(String key) {
        return Component.translatable(key).withStyle(ChatFormatting.BOLD);
    }
}
