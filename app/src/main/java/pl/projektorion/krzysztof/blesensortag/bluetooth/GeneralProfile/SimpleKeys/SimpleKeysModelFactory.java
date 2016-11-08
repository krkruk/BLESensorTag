package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ModelFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class SimpleKeysModelFactory implements ModelFactory {
    public SimpleKeysModelFactory() {}

    @Override
    public GenericGattObserverInterface createObserver() {
        return new SimpleKeysModel();
    }
}
