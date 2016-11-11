package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericProfileFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadProfileInterface;

/**
 * Created by krzysztof on 11.11.16.
 */

public class DeviceInformationGenericProfileFactory implements GenericProfileFactory {
    private BLeGattIO gattIO;

    public DeviceInformationGenericProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new DeviceInformationReadProfile(gattIO);
    }
}
