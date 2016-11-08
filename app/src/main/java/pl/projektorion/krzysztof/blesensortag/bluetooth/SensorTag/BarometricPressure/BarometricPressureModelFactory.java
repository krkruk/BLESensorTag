package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ModelFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class BarometricPressureModelFactory implements ModelFactory {
    public BarometricPressureModelFactory() {}

    @Override
    public GenericGattNotifyModelInterface createObserver() {
        return new BarometricPressureModel();
    }
}
