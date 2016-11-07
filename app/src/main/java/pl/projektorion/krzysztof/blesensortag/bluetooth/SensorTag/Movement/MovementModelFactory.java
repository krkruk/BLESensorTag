package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ModelFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class MovementModelFactory implements ModelFactory {
    public MovementModelFactory() {}

    @Override
    public GenericGattObserverInterface createObserver() {
        return new MovementModel();
    }
}
