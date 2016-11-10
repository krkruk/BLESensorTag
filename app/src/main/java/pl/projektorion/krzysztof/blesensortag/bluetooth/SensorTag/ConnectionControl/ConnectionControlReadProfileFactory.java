package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.ProfileReadFactory;

/**
 * Created by krzysztof on 10.11.16.
 */

public class ConnectionControlReadProfileFactory implements ProfileReadFactory {
    private BLeGattIO gattIO;

    public ConnectionControlReadProfileFactory(BLeGattIO gattIO) {
        this.gattIO = gattIO;
    }

    @Override
    public GenericGattReadProfileInterface createProfile() {
        return new ConnectionControlReadProfile(gattIO);
    }
}
