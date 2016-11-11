package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericProfileFactory;

/**
 * Created by krzysztof on 11.11.16.
 */

public class HumidityGenericProfileFactory implements GenericProfileFactory {
    private BLeGattIO gattIO;

    public HumidityGenericProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new HumidityNotifyProfile(gattIO);
    }
}
