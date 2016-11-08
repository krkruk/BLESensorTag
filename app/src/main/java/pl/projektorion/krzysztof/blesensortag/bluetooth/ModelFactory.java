package pl.projektorion.krzysztof.blesensortag.bluetooth;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;

/**
 * Created by krzysztof on 06.11.16.
 */

public interface ModelFactory {
    GenericGattObserverInterface createObserver();
}
