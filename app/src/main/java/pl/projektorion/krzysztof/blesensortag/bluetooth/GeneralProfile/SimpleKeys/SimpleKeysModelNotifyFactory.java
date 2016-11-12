package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ModelNotifyFactory;

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
