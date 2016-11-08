package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ModelFactory;

/**
 * Created by krzysztof on 06.11.16.
 */

public class IRTemperatureModelFactory implements ModelFactory {
    public IRTemperatureModelFactory() {}

    @Override
    public GenericGattObserverInterface createObserver() {
        return new IRTemperatureModel();
    }
}
