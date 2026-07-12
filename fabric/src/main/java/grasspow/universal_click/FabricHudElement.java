package grasspow.universal_click;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;

public class FabricHudElement implements HudElement {

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        HudRenderer hudRenderer = new HudRenderer(Minecraft.getInstance());
        hudRenderer.render(graphics);
    }

}
