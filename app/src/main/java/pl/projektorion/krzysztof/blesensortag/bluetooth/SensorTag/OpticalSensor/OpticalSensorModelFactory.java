package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ModelFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class OpticalSensorModelFactory implements ModelFactory {
    public OpticalSensorModelFactory() {}

    @Override
    public GenericGattObserverInterface createObserver() {
        return new OpticalSensorModel();
    }
}
