package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ModelFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class BarometricPressureModelFactory implements ModelFactory {
    public BarometricPressureModelFactory() {}

    @Override
    public GenericGattObserverInterface createObserver() {
        return new BarometricPressureModel();
    }
}
