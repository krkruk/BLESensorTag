package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;

/**
 * Created by krzysztof on 02.11.16.
 */

public interface ProfileFactory {
    GenericGattProfileInterface createProfile();

    GenericGattObserverInterface createObserver();
}
