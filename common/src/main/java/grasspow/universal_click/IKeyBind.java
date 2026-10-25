package grasspow.universal_click;

/**
 * @author tbsc on 02/09/2021
 */
public interface IKeyBind {

    void setHeld(boolean held);

    boolean isDown();

    boolean consumeClick();

}
