package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.GenericGattNotifyModelInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ModelNotifyFactory;

/**
 * Created by krzysztof on 07.11.16.
 */

public class MovementModelNotifyFactory implements ModelNotifyFactory {
    public MovementModelNotifyFactory() {}

    @Override
    public GenericGattNotifyModelInterface createObserver() {
        return new MovementModel();
    }
}
