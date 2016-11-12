package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ModelNotifyFactory;

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
