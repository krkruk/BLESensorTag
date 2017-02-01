package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.HeartRate;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericProfileFactory;

/**
 * Created by krzysztof on 01.02.17.
 */

public class HeartRateGenericProfileFactory implements GenericProfileFactory {
    private BLeGattIO gattIO;

    public HeartRateGenericProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new HeartRateProfile(gattIO);
    }
}
