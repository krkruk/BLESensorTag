package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileNotifyFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class OpticalSensorProfileNotifyFactory implements ProfileNotifyFactory {
    private BLeGattIO gattIO;

    public OpticalSensorProfileNotifyFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public NotifyGattProfileInterface createProfile() {
        return new OpticalSensorProfile(gattIO);
    }
}
