package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ModelNotifyFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class OpticalSensorModelNotifyFactory implements GenericModelFactory {
    public OpticalSensorModelNotifyFactory() {}

    @Override
    public GenericGattModelInterface createModel() {
        return new OpticalSensorModel();
    }
}
