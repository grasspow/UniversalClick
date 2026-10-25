package grasspow.universal_click.fabric;

import grasspow.universal_click.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public class UniversalClickFabricClient implements ClientModInitializer {


    private ClickHandler clickHandler;
    private boolean initialized = false;

    @Override
    public void onInitializeClient() {
        KeyMappings.initKeybindings(KeyMappingHelper::registerKeyMapping);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            lazyInit(client);
            if (clickHandler != null) {
                clickHandler.onTick();
            }
        });

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            lazyInit(client);
            if (clickHandler != null) {
                clickHandler.pollKeyBindings();
            }
        });

        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (clickHandler != null) {
                clickHandler.onInitGuiPre();
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (clickHandler != null) {
                clickHandler.reset();
            }
        });

        HudElementRegistry.attachElementAfter(
                VanillaHudElements.MISC_OVERLAYS,
                Identifier.fromNamespaceAndPath(UniversalClick.MOD_ID, "hud_overlay"),
                new FabricHudElement()
        );
        VersionCompat.init();
    }

    private void lazyInit(Minecraft client) {
        if (initialized) return;
        initialized = true;

        FabricConfig config = new FabricConfig();
        clickHandler = new ClickHandler(client, config);

        ClickHandlerAccess.set(clickHandler);
    }
}
