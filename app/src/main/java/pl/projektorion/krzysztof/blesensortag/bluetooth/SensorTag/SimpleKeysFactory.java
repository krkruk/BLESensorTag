package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;

/**
 * Created by krzysztof on 02.11.16.
 */

public class SimpleKeysFactory implements ProfileFactory {
    BLeGattIO gattClient;

    public SimpleKeysFactory(BLeGattIO gattClient) {
        this.gattClient = gattClient;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new SimpleKeysProfile(gattClient);
    }

    @Override
    public GenericGattObserverInterface createObserver() {
        return new SimpleKeysModel();
    }
}
