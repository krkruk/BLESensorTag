package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericModelFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class BarometricPressureModelNotifyFactory implements GenericModelFactory {
    public BarometricPressureModelNotifyFactory() {}

    @Override
    public GenericGattModelInterface createModel() {
        return new BarometricPressureModel();
    }
}
