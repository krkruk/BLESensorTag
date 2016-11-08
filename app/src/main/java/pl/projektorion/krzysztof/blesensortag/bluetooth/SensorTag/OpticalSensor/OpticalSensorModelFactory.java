package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ModelFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class OpticalSensorModelFactory implements ModelFactory {
    public OpticalSensorModelFactory() {}

    @Override
    public GenericGattNotifyModelInterface createObserver() {
        return new OpticalSensorModel();
    }
}
