package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericModelFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class SimpleKeysModelNotifyFactory implements GenericModelFactory {
    public SimpleKeysModelNotifyFactory() {}

    @Override
    public GenericGattModelInterface createModel() {
        return new SimpleKeysModel();
    }
}
