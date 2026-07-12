package grasspow.universal_click;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class KeyMappings {

    private static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(UniversalClick.MOD_ID, "key_categories"));

    public static KeyMapping KEY_TOGGLE_RIGHT;
    public static KeyMapping KEY_TOGGLE_LEFT;
    public static KeyMapping KEY_TOGGLE_SMART_ATTACK;
    public static KeyMapping KEY_TOGGLE_HOLD_RIGHT;
    public static KeyMapping KEY_SPEED;

    public static void initKeybindings(Consumer<KeyMapping> consumer) {
        KEY_TOGGLE_RIGHT = new KeyMapping("key." + UniversalClick.MOD_ID + ".toggleright", GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
        KEY_TOGGLE_LEFT = new KeyMapping("key." + UniversalClick.MOD_ID + ".toggleleft", GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
        KEY_TOGGLE_SMART_ATTACK = new KeyMapping("key." + UniversalClick.MOD_ID + ".togglesmartattack", GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
        KEY_TOGGLE_HOLD_RIGHT = new KeyMapping("key." + UniversalClick.MOD_ID + ".toggleholdright", GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);
        KEY_SPEED = new KeyMapping("key." + UniversalClick.MOD_ID + ".speed", GLFW.GLFW_KEY_UNKNOWN, KEY_CATEGORY);

        consumer.accept(KEY_TOGGLE_RIGHT);
        consumer.accept(KEY_TOGGLE_LEFT);
        consumer.accept(KEY_TOGGLE_SMART_ATTACK);
        consumer.accept(KEY_TOGGLE_HOLD_RIGHT);
        consumer.accept(KEY_SPEED);
    }
}
