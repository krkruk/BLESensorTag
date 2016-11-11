package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericProfileFactory;

/**
 * Created by krzysztof on 11.11.16.
 */

public class BarometricPressureGenericProfileFactory implements GenericProfileFactory {
    private BLeGattIO gattIO;

    public BarometricPressureGenericProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new BarometricPressureProfile(gattIO);
    }
}
