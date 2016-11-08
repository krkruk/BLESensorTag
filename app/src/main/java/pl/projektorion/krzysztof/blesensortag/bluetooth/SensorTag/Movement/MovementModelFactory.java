package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.ModelFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class MovementModelFactory implements ModelFactory {
    public MovementModelFactory() {}

    @Override
    public GenericGattNotifyModelInterface createObserver() {
        return new MovementModel();
    }
}
