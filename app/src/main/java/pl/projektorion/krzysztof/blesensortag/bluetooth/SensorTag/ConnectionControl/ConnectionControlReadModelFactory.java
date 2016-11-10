package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl;

import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.GenericGattReadProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.ModelReadFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.read.ProfileReadFactory;

/**
 * Created by krzysztof on 10.11.16.
 */

public class ConnectionControlReadModelFactory implements ModelReadFactory {
    public ConnectionControlReadModelFactory() {}

    @Override
    public GenericGattReadModelInterface createModel() {
        return new ConnectionControlReadModel();
    }
}
