package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ModelFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class SimpleKeysModelFactory implements ModelFactory {
    public SimpleKeysModelFactory() {}

    @Override
    public GenericGattNotifyModelInterface createObserver() {
        return new SimpleKeysModel();
    }
}
