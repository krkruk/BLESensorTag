package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericModelFactory;

/**
 * Created by krzysztof on 09.11.16.
 */

public class DeviceInformationReadModelFactory implements GenericModelFactory {
    public DeviceInformationReadModelFactory() {}

    @Override
    public GenericGattModelInterface createModel() {
        return new DeviceInformationReadModel();
    }
}
