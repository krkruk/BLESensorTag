package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ModelNotifyFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class IRTemperatureModelNotifyFactory implements ModelNotifyFactory {
    public IRTemperatureModelNotifyFactory() {}

    @Override
    public GenericGattNotifyModelInterface createObserver() {
        return new IRTemperatureModel();
    }
}
