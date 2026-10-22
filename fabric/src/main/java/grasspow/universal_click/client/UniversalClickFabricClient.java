package grasspow.universal_click.client;

import grasspow.universal_click.*;
import grasspow.universal_click.config.FabricConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import static grasspow.universal_click.KeyMappings.*;

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
                while (KEY_TOGGLE_RIGHT.consumeClick()) {
                    clickHandler.onKeyPressed();
                }
                while (KEY_TOGGLE_LEFT.consumeClick()) {
                    clickHandler.onKeyPressed();
                }
                while (KEY_TOGGLE_SMART_ATTACK.consumeClick()) {
                    clickHandler.onKeyPressed();
                }
                while (KEY_TOGGLE_HOLD_RIGHT.consumeClick()) {
                    clickHandler.onKeyPressed();
                }
                while (KEY_SPEED.consumeClick()) {
                    clickHandler.onKeyPressed();
                }
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
        VersionCompact.init();
    }

    private void lazyInit(Minecraft client) {
        if (initialized) return;
        initialized = true;

        FabricConfig config = new FabricConfig();
        clickHandler = new ClickHandler(
                client,
                config,
                new FabricClickInputHandler()
        );

        ClickHandlerAccess.set(clickHandler);
    }
}
