package grasspow.universal_click;

public class FabricClickInputHandler implements IClickInputHandler {

    @Override
    public boolean postAttackInputEvent() {
        // Fabric has no direct equivalent of NeoForge's ClientHooks.onClickInput,
        // so we always allow swinging the hand.
        return true;
    }

}
