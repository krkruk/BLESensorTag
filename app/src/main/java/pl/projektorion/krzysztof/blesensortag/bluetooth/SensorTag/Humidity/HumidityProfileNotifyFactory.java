package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileNotifyFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class HumidityProfileNotifyFactory implements ProfileNotifyFactory {
    private BLeGattIO gattIO;

    public HumidityProfileNotifyFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public NotifyGattProfileInterface createProfile() {
        return new HumidityProfile(gattIO);
    }
}
