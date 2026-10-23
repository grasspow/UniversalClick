package grasspow.universal_click.mixin;

import grasspow.universal_click.UniversalClick;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Applies version-sensitive mixins only when their target methods exist.
 *
 * <p>This lets a single jar stay loadable on future Minecraft versions where
 * an internal method was renamed or removed. The related feature degrades,
 * but the client does not crash during mixin application.</p>
 */
public final class UniversalClickMixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith(".MinecraftMixin")) {
            try {
                Minecraft.class.getDeclaredMethod("continueAttack", boolean.class);
                return true;
            } catch (ReflectiveOperationException | LinkageError e) {
                UniversalClick.LOG.warn(
                        "[UniversalClick] Minecraft#continueAttack(boolean) not found; "
                                + "auto-left mining support is disabled on this version"
                );
                return false;
            }
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
