package grasspow.universal_click;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class VersionCompact {

    public static Field hudField;
    public static Method getChatMethod;
    private static Method guiGetChatMethod;
    private static Field screenField;
    private static Method screenMethod;
    static final String MC_VERSION = SharedConstants.getCurrentVersion().name();

    public static void init() {
        UniversalClick.LOG.info("Initializing version compact, minecraft version: " + MC_VERSION);

        // 26.1 keeps the screen field on Minecraft; 26.2+ moved it to Gui.
        try {
            screenMethod = Gui.class.getMethod("screen");
        } catch (NoSuchMethodException e) {
            try {
                screenField = Minecraft.class.getField("screen");
            } catch (NoSuchFieldException ex) {
                UniversalClick.LOG.error("Couldn't find Minecraft screen field or Gui#screen method");
            }
        }

        // 26.2+ exposes chat through Gui.hud#getChat; older versions expose
        // Gui#getChat directly. Detect by capability instead of version name.
        try {
            hudField = Gui.class.getField("hud");
            getChatMethod = hudField.getType().getMethod("getChat");
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            try {
                guiGetChatMethod = Gui.class.getMethod("getChat");
            } catch (NoSuchMethodException ex) {
                UniversalClick.LOG.error("Couldn't find a chat accessor for Minecraft " + MC_VERSION);
            }
        }
    }

    public static Screen getScreen(Minecraft mc) {
        if (mc.gui == null) {
            return null;
        }

        try {
            if (screenMethod != null) {
                return (Screen) screenMethod.invoke(mc.gui);
            }
            if (screenField != null) {
                return (Screen) screenField.get(mc);
            }
        } catch (Exception e) {
            UniversalClick.LOG.error("Error while getting current screen: {}", e.getMessage());
        }
        return null;
    }

    public static ChatComponent getChat(Minecraft mc) {
        if (mc.gui == null) {
            return null;
        }

        try {
            if (hudField != null && getChatMethod != null) {
                Object hud = hudField.get(mc.gui);
                if (hud != null) {
                    return (ChatComponent) getChatMethod.invoke(hud);
                }
            } else if (guiGetChatMethod != null) {
                return (ChatComponent) guiGetChatMethod.invoke(mc.gui);
            }
        } catch (Exception e) {
            UniversalClick.LOG.error("Error while getting ChatComponent: {}", e.getMessage());
        }
        return null;
    }
}
