package pl.projektorion.krzysztof.blesensortag.bluetooth.reading;

import android.bluetooth.BluetoothGattCharacteristic;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattModelInterface;


/**
 * Created by krzysztof on 08.11.16.
 */

public interface GenericGattReadModelInterface extends GenericGattModelInterface {

    /**
     * Check whether the object can be associated with the {@param characteristic}
     * @param characteristic {@link BluetoothGattCharacteristic} to be checked
     *                                                          whether its UUID can be associated
     *                                                          with the instance of the object
     * @return boolean - true if the object has a UUID described in {@param characteristic}
     */
    boolean hasCharacteristic(BluetoothGattCharacteristic characteristic);
}
