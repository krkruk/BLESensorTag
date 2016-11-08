package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ModelNotifyFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class OpticalSensorModelNotifyFactory implements ModelNotifyFactory {
    public OpticalSensorModelNotifyFactory() {}

    @Override
    public GenericGattNotifyModelInterface createObserver() {
        return new OpticalSensorModel();
    }
}
