package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericModelFactory;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ModelNotifyFactory;

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
