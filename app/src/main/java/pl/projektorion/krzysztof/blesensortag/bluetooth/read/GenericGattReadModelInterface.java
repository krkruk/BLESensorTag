package pl.projektorion.krzysztof.blesensortag.bluetooth.read;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;


/**
 * Created by krzysztof on 08.11.16.
 */

public interface GenericGattReadModelInterface {

    /**
     * Update model state if any new data has been retrieved.
     * @param characteristic A new {@link BluetoothGattCharacteristic} object containing
     *                       updated data
     */
    void updateCharacteristic(BluetoothGattCharacteristic characteristic);

    /**
     * Check whether the object can be associated with the {@param characteristic}
     * @param characteristic {@link BluetoothGattCharacteristic} to be checked
     *                                                          whether its UUID can be associated
     *                                                          with the instance of the object
     * @return boolean - true if the object has a UUID described in {@param characteristic}
     */
    boolean hasCharacteristic(BluetoothGattCharacteristic characteristic);

    /**
     * Get data as a container object
     * @return {@link Object} Container object. Casting to the proper datatype might be
     * needed
     */
    Object getData();
}
