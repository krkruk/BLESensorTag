package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ModelNotifyFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class BarometricPressureModelNotifyFactory implements ModelNotifyFactory {
    public BarometricPressureModelNotifyFactory() {}

    @Override
    public GenericGattNotifyModelInterface createObserver() {
        return new BarometricPressureModel();
    }
}
