package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.ProfileReadFactory;

/**
 * Created by krzysztof on 09.11.16.
 */

public class DeviceInformationReadProfileFactory implements ProfileReadFactory {
    private BLeGattIO gattIO;

    public DeviceInformationReadProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattReadProfileInterface createProfile() {
        return new DeviceInformationReadProfile(gattIO);
    }
}
