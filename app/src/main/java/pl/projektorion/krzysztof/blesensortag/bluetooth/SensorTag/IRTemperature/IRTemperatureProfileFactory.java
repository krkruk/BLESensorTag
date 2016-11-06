package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ProfileFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class IRTemperatureProfileFactory implements ProfileFactory {
    BLeGattIO gattIO;

    public IRTemperatureProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new IRTemperatureProfile(gattIO);
    }
}
