package pl.projektorion.krzysztof.blesensortag.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by krzysztof on 12.11.16.
 */

public interface GenericGattModelInterface {
    /**
     * Update model state if any new data has been retrieved.
     * @param characteristic A new {@link BluetoothGattCharacteristic} object containing
     *                       updated data
     */
    void updateCharacteristic(BluetoothGattCharacteristic characteristic);

    /**
     * Get processed data from the BLE device. The data is
     * embraced into a profile data container.
     * @return Return Object. Must be then casted into
     * a proper object
     */
    Object getData();
}
