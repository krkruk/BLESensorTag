package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileNotifyFactory;

/**
 * Created by krzysztof on 03.11.16.
 */

public class BarometricPressureProfileNotifyFactory implements ProfileNotifyFactory {
    private BLeGattIO gattClient;

    public BarometricPressureProfileNotifyFactory(BLeGattIO gattClient) {
        this.gattClient = gattClient;
    }

    @Override
    public NotifyGattProfileInterface createProfile() {
        return new BarometricPressureProfile(gattClient);
    }
}
