package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ProfileFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class OpticalSensorProfileFactory implements ProfileFactory {
    private BLeGattIO gattIO;

    public OpticalSensorProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new OpticalSensorProfile(gattIO);
    }
}
