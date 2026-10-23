package grasspow.universal_click;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.SharedConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Central place for Minecraft APIs that are not stable across 26.x versions.
 *
 * <p>Everything here must be resolved by capability detection instead of
 * exact version strings. If an API is unavailable the related feature is
 * disabled with a warning instead of crashing the client.</p>
 */
public final class VersionCompat {

    private static final String MC_VERSION = SharedConstants.getCurrentVersion().name();
    private static boolean initialized;

    private static Field screenField;
    private static Method screenMethod;

    private static Field hudField;
    private static Method getChatMethod;
    private static Method guiGetChatMethod;

    private static Method startAttackMethod;
    private static Method continueAttackMethod;
    private static Method startUseItemMethod;
    private static Field rightClickDelayField;
    private static Field missTimeField;
    private static Field keyMappingKeyField;

    private VersionCompat() {
    }

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        UniversalClick.LOG.info("Initializing version compatibility, minecraft version: " + MC_VERSION);

        // 26.1 exposes Minecraft.screen. 26.2+ moved it to Gui#screen().
        try {
            screenMethod = Gui.class.getMethod("screen");
        } catch (NoSuchMethodException e) {
            screenField = findField(Minecraft.class, "screen");
        }

        // 26.2+ exposes chat through Gui.hud#getChat; 26.1 uses Gui#getChat.
        try {
            hudField = Gui.class.getField("hud");
            getChatMethod = hudField.getType().getMethod("getChat");
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            try {
                guiGetChatMethod = Gui.class.getMethod("getChat");
            } catch (NoSuchMethodException ex) {
                warn("Couldn't find a chat accessor for Minecraft " + MC_VERSION);
            }
        }

        startAttackMethod = findMethod(Minecraft.class, "startAttack");
        continueAttackMethod = findMethod(Minecraft.class, "continueAttack", boolean.class);
        startUseItemMethod = findMethod(Minecraft.class, "startUseItem");
        rightClickDelayField = findField(Minecraft.class, "rightClickDelay");
        missTimeField = findField(Minecraft.class, "missTime");
        keyMappingKeyField = findField(KeyMapping.class, "key");
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
            warn("Error while getting current screen: " + e.getMessage());
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
            warn("Error while getting ChatComponent: " + e.getMessage());
        }
        return null;
    }

    public static boolean startAttack(Minecraft mc) {
        if (startAttackMethod == null) {
            return false;
        }
        try {
            startAttackMethod.invoke(mc);
            return true;
        } catch (Exception e) {
            warn("Error while invoking startAttack: " + e.getMessage());
            return false;
        }
    }

    public static void continueAttack(Minecraft mc, boolean down) {
        if (continueAttackMethod == null) {
            return;
        }
        try {
            continueAttackMethod.invoke(mc, down);
        } catch (Exception e) {
            warn("Error while invoking continueAttack: " + e.getMessage());
        }
    }

    public static void startUseItem(Minecraft mc) {
        if (startUseItemMethod == null) {
            return;
        }
        try {
            startUseItemMethod.invoke(mc);
        } catch (Exception e) {
            warn("Error while invoking startUseItem: " + e.getMessage());
        }
    }

    public static int getRightClickDelay(Minecraft mc) {
        if (rightClickDelayField == null) {
            return 0;
        }
        try {
            return rightClickDelayField.getInt(mc);
        } catch (Exception e) {
            warn("Error while reading rightClickDelay: " + e.getMessage());
            return 0;
        }
    }

    public static void setMissTime(Minecraft mc, int value) {
        if (missTimeField == null) {
            return;
        }
        try {
            missTimeField.setInt(mc, value);
        } catch (Exception e) {
            warn("Error while setting missTime: " + e.getMessage());
        }
    }

    public static InputConstants.Key getKeyMappingKey(KeyMapping mapping) {
        if (keyMappingKeyField == null) {
            return null;
        }
        try {
            return (InputConstants.Key) keyMappingKeyField.get(mapping);
        } catch (Exception e) {
            warn("Error while reading KeyMapping.key: " + e.getMessage());
            return null;
        }
    }

    private static Method findMethod(Class<?> owner, String name, Class<?>... parameters) {
        try {
            Method method = owner.getDeclaredMethod(name, parameters);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            warn("Couldn't find " + owner.getName() + "#" + name);
            return null;
        }
    }

    private static Field findField(Class<?> owner, String name) {
        try {
            Field field = owner.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            warn("Couldn't find " + owner.getName() + "." + name);
            return null;
        }
    }

    private static void warn(String message) {
        UniversalClick.LOG.warn("[UniversalClick] {}", message);
    }
}
