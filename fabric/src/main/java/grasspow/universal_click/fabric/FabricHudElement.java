package grasspow.universal_click.fabric;

import grasspow.universal_click.HudRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;

public class FabricHudElement implements HudElement {

    private HudRenderer hudRenderer;

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        if (hudRenderer == null) {
            hudRenderer = new HudRenderer(Minecraft.getInstance());
        }
        hudRenderer.render(graphics);
    }

}
