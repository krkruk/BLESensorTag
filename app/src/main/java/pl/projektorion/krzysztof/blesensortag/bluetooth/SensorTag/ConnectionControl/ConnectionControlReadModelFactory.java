package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericModelFactory;

/**
 * Created by krzysztof on 10.11.16.
 */

public class ConnectionControlReadModelFactory implements GenericModelFactory {
    public ConnectionControlReadModelFactory() {}

    @Override
    public GenericGattModelInterface createModel() {
        return new ConnectionControlReadModel();
    }
}
