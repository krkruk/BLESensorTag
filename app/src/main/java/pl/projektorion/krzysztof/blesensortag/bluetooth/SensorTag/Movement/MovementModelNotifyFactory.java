package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericModelFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class MovementModelNotifyFactory implements GenericModelFactory {
    public MovementModelNotifyFactory() {}

    @Override
    public GenericGattModelInterface createModel() {
        return new MovementModel();
    }
}
