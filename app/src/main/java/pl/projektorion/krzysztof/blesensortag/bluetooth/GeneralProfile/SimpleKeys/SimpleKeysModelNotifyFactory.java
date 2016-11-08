package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ModelNotifyFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class SimpleKeysModelNotifyFactory implements ModelNotifyFactory {
    public SimpleKeysModelNotifyFactory() {}

    @Override
    public GenericGattNotifyModelInterface createObserver() {
        return new SimpleKeysModel();
    }
}
