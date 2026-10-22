package grasspow.universal_click.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

    @Invoker("startUseItem")
    void invokeStartUseItem();

    @Invoker("startAttack")
    boolean invokeStartAttack();

    @Invoker("continueAttack")
    void invokeContinueAttack(boolean down);

    @Accessor("rightClickDelay")
    int getRightClickDelay();

    @Accessor("rightClickDelay")
    void setRightClickDelay(int delay);

    @Accessor("missTime")
    void setMissTime(int missTime);

}
