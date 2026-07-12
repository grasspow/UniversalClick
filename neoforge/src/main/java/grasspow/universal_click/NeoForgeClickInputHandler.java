package grasspow.universal_click;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.client.ClientHooks;

public class NeoForgeClickInputHandler implements IClickInputHandler {

    @Override
    public boolean postAttackInputEvent() {
        var event = ClientHooks.onClickInput(0, Minecraft.getInstance().options.keyAttack, InteractionHand.MAIN_HAND);
        return event.shouldSwingHand();
    }

}
