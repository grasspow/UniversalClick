package grasspow.universal_click;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.ChatComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class VersionCompact {

    // 26.2
    public static Class<?> hudClazz;
    public static Field hudField;
    public static Method getChatMethod;
    static final String MC_VERSION = SharedConstants.getCurrentVersion().name();

    public static void init() {
        if (MC_VERSION.equals("26.2")) {
            try {
                hudClazz = Class.forName("net.minecraft.client.gui.Hud");
                hudField = Gui.class.getField("hud");
                getChatMethod = hudClazz.getMethod("getChat");
            } catch (ClassNotFoundException e) {
                UniversalClick.LOG.error("Couldn't find class for Minecraft 26.2: \"net.minecraft.client.gui.Hud\"");
                UniversalClick.LOG.error(e.getMessage());
            } catch (NoSuchFieldException e) {
                UniversalClick.LOG.error("Couldn't find Field for Minecraft 26.2: \"net.minecraft.client.gui.Gui.hud\"");
            } catch (NoSuchMethodException e) {
                UniversalClick.LOG.error("Couldn't find Method for Minecraft 26.2: \"net.minecraft.client.gui.Hud#getChat\"");
            }
        }
    }

    public static ChatComponent getChat(Minecraft mc) {
        switch (MC_VERSION) {
            case "26.2" -> {
                if (hudField == null) return null;
                try {
                    Object o = hudField.get(mc.gui);
                    if (o != null) {
                        return (ChatComponent) getChatMethod.invoke(o);
                    }
                } catch (Exception e) {
                    UniversalClick.LOG.error("Error while getting ChatComponent from Minecraft 26.2: {}", e.getMessage());
                }
            }
            default -> {
                return mc.gui.getChat();
            }
        }
        return null;
    }
}
