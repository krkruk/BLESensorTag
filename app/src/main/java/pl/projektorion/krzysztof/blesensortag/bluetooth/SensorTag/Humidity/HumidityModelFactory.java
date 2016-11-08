package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ModelFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class HumidityModelFactory implements ModelFactory {
    public HumidityModelFactory() {}

    @Override
    public GenericGattNotifyModelInterface createObserver() {
        return new HumidityModel();
    }
}
