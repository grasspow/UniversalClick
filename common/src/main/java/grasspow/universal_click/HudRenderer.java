package grasspow.universal_click;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public class HudRenderer {

    private final Minecraft minecraft;

    public HudRenderer(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void render(GuiGraphicsExtractor graphics) {
        ClickHandler handler = ClickHandlerAccess.get();
        if (handler == null) return;

        ClickHandler.HudTexts hudTexts = handler.getHudTexts();
        if (hudTexts.isEmpty()) return;

        var texts = hudTexts.texts();
        for (int i = 0; i < texts.size(); ++i) {
            String text = texts.get(i);
            graphics.text(minecraft.font, Component.literal(ChatFormatting.BOLD + text), 6, 6 + 10 * i, 0xFFFF0000);
        }
    }
}
