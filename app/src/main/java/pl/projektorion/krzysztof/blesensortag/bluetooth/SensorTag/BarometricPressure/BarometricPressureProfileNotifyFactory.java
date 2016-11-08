package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileNotifyFactory;

/**
 * Created by krzysztof on 03.11.16.
 */

public class BarometricPressureProfileNotifyFactory implements ProfileNotifyFactory {
    private BLeGattIO gattClient;

    public BarometricPressureProfileNotifyFactory(BLeGattIO gattClient) {
        this.gattClient = gattClient;
    }

    @Override
    public GenericGattNotifyProfileInterface createProfile() {
        return new BarometricPressureNotifyProfile(gattClient);
    }
}
