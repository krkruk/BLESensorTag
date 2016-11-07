package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ModelFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class HumidityModelFactory implements ModelFactory {
    public HumidityModelFactory() {}

    @Override
    public GenericGattObserverInterface createObserver() {
        return new HumidityModel();
    }
}
