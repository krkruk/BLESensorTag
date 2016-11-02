package pl.projektorion.krzysztof.blesensortag.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import java.util.UUID;

/**
 * Created by krzysztof on 02.11.16.
 */


/**
 * Observer pattern for SensorTag profiles. Here: interface for an observer
 */
public interface GenericGattObserverInterface {

    /**
     * Update Characteristic
     * @param characteristic {@link BluetoothGattCharacteristic} data changed
     */
    void updateCharacteristic(BluetoothGattCharacteristic characteristic);

    /**
     * Get UUID that contains data
     * @return {@link} UUID for comparison
     */
    UUID getDataUuid();

    /**
     * Get unprocessed data received from the BLE device
     * @return Byte array data set.
     */
    byte[] getRawData();

    /**
     * Get processed data from the BLE device. The data is
     * embraced into a profile data container.
     * @return Return Object. Must be then casted into
     * a proper object
     */
    Object getData();
}
