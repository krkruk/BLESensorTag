package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericModelFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class HumidityModelNotifyFactory implements GenericModelFactory {
    public HumidityModelNotifyFactory() {}

    @Override
    public GenericGattModelInterface createModel() {
        return new HumidityModel();
    }
}
