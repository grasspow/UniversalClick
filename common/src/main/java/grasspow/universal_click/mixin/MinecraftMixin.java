package grasspow.universal_click.mixin;

import grasspow.universal_click.ClickHandler;
import grasspow.universal_click.ClickHandlerAccess;
import grasspow.universal_click.VersionCompat;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Inject(method = "continueAttack(Z)V", at = @At("HEAD"), cancellable = true)
    private void universal_click$keepAutoLeftMining(boolean down, CallbackInfo ci) {
        ClickHandler handler = ClickHandlerAccess.get();
        if (handler != null && handler.shouldOwnLeftClick() && !handler.isInternalContinue()) {
            // Vanilla stops block breaking on every tick when the physical
            // attack key is not held. Auto-left owns this state instead, but
            // still needs the missTime reset that the stop path performs.
            if (!down) {
                VersionCompat.setMissTime((Minecraft) (Object) this, 0);
            }
            ci.cancel();
        }
    }
}
