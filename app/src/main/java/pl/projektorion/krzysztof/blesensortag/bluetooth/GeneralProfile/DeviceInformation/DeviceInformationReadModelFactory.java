package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.DeviceInformation;

import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.ModelReadFactory;

/**
 * Created by krzysztof on 09.11.16.
 */

public class DeviceInformationReadModelFactory implements ModelReadFactory {
    public DeviceInformationReadModelFactory() {}

    @Override
    public GenericGattReadModelInterface createModel() {
        return new DeviceInformationReadModel();
    }
}
