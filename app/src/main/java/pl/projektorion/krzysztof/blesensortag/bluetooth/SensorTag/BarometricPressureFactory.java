package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.util.Log;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;

/**
 * Created by krzysztof on 03.11.16.
 */

public class BarometricPressureFactory implements ProfileFactory {
    private BLeGattIO gattClient;

    public BarometricPressureFactory(BLeGattIO gattClient) {
        this.gattClient = gattClient;
    }

    @Override
    public GenericGattObserverInterface createObserver() {
        return new BarometricPressureModel();
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new BarometricPressureProfile(gattClient);
    }
}
