package grasspow.universal_click.client;

import grasspow.universal_click.*;
import grasspow.universal_click.config.NeoForgeConfig;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = UniversalClick.MOD_ID,dist = Dist.CLIENT)
public class UniversalClickNeoForgeClient {

    private HudRenderer hudRenderer;
    private ClickHandler clickHandler;
    private boolean initialized = false;

    public UniversalClickNeoForgeClient(ModContainer container, IEventBus bus) {
        container.registerConfig(ModConfig.Type.CLIENT, NeoForgeConfig.CONFIG_SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        bus.addListener(this::onRegisterKeyMappings);
        bus.addListener(this::onConfigLoad);

        NeoForge.EVENT_BUS.register(this);
        VersionCompact.init();
    }

    private void onConfigLoad(ModConfigEvent event) {
    }

    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        KeyMappings.initKeybindings(event::register);
    }

    private void lazyInit() {
        if (initialized) return;
        initialized = true;

        Minecraft mc = Minecraft.getInstance();

        clickHandler = new ClickHandler(
                mc,
                NeoForgeConfig.CONFIG,
                new NeoForgeClickInputHandler()
        );

        ClickHandlerAccess.set(clickHandler);
    }

    @SubscribeEvent
    private void onClientTick(ClientTickEvent.Post event) {
        lazyInit();
        if (clickHandler != null) {
            clickHandler.onTick();
        }
    }

    @SubscribeEvent
    private void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        if (clickHandler != null) {
            clickHandler.reset();
        }
    }

    @SubscribeEvent
    private void onKeyInput(InputEvent.Key event) {
        lazyInit();
        if (clickHandler != null) {
            clickHandler.onKeyPressed();
        }
    }

    @SubscribeEvent
    private void onScreenInitPre(ScreenEvent.Init.Pre event) {
        lazyInit();
        if (clickHandler != null) {
            clickHandler.onInitGuiPre();
        }
    }

    @SubscribeEvent
    private void onRenderGuiLayer(RenderGuiLayerEvent.Post event) {
        if (hudRenderer == null) {
            Minecraft mc = Minecraft.getInstance();
            hudRenderer = new HudRenderer(mc);
        }
        hudRenderer.render(event.getGuiGraphics());
    }
}
