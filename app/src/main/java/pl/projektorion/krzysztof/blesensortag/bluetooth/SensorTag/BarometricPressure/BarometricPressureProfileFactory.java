package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattNotifyProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ProfileFactory;

/**
 * Created by krzysztof on 03.11.16.
 */

public class BarometricPressureProfileFactory implements ProfileFactory {
    private BLeGattIO gattClient;

    public BarometricPressureProfileFactory(BLeGattIO gattClient) {
        this.gattClient = gattClient;
    }

    @Override
    public GenericGattNotifyProfileInterface createProfile() {
        return new BarometricPressureNotifyProfile(gattClient);
    }
}
