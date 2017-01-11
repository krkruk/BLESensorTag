package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericProfileFactory;

/**
 * Created by krzysztof on 11.11.16.
 */

public class OpticalSensorGenericProfileFactory implements GenericProfileFactory {
    private BLeGattIO gattIO;

    public OpticalSensorGenericProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new OpticalSensorProfile(gattIO);
    }
}
