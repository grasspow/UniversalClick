package grasspow.universal_click.impl;

import grasspow.universal_click.IKeyBind;
import com.mojang.blaze3d.platform.InputConstants;
import grasspow.universal_click.mixin.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;

/**
 * @author tbsc on 02/09/2021
 */
public class KeyBind implements IKeyBind {

    private final KeyMapping key;

    public KeyBind(KeyMapping key) {
        this.key = key;
    }

    @Override
    public void setHeld(boolean held) {
        InputConstants.Key inputKey = ((KeyMappingAccessor) key).getMappingKey();
        KeyMapping.set(inputKey, held);
    }

    @Override
    public boolean isDown() {
        return key.isDown();
    }

}
