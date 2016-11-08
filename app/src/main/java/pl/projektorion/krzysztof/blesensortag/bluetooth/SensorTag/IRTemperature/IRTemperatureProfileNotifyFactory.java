package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileNotifyFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class IRTemperatureProfileNotifyFactory implements ProfileNotifyFactory {
    BLeGattIO gattIO;

    public IRTemperatureProfileNotifyFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattNotifyProfileInterface createProfile() {
        return new IRTemperatureNotifyProfile(gattIO);
    }
}
