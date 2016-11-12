package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ModelNotifyFactory;

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
