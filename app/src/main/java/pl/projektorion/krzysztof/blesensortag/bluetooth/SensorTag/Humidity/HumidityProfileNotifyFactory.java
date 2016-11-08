package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileNotifyFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class HumidityProfileNotifyFactory implements ProfileNotifyFactory {
    private BLeGattIO gattIO;

    public HumidityProfileNotifyFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattNotifyProfileInterface createProfile() {
        return new HumidityNotifyProfile(gattIO);
    }
}
