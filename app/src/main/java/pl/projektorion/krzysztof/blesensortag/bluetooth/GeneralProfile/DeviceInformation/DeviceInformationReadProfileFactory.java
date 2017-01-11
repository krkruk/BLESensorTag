package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.reading.GenericGattReadProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.reading.ProfileReadFactory;

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
