package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericModelFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class IRTemperatureModelNotifyFactory implements GenericModelFactory {
    public IRTemperatureModelNotifyFactory() {}

    @Override
    public GenericGattModelInterface createModel() {
        return new IRTemperatureModel();
    }
}
