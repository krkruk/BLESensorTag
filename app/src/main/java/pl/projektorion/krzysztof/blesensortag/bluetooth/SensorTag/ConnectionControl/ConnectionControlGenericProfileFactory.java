package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericProfileFactory;

/**
 * Created by krzysztof on 11.11.16.
 */

public class ConnectionControlGenericProfileFactory implements GenericProfileFactory {
    private BLeGattIO gattIO;

    public ConnectionControlGenericProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattProfileInterface createProfile() {
        return new ConnectionControlReadProfile(gattIO);
    }
}
