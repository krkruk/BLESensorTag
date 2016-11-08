package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattNotifyProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ProfileFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class HumidityProfileFactory implements ProfileFactory {
    private BLeGattIO gattIO;

    public HumidityProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattNotifyProfileInterface createProfile() {
        return new HumidityNotifyProfile(gattIO);
    }
}
