package pl.projektorion.krzysztof.blesensortag.bluetooth.notify;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattModelInterface;

/**
 * Created by krzysztof on 02.11.16.
 */


/**
 * Observer pattern for SensorTag profiles. Here: interface for an observer
 */
public interface GenericGattNotifyModelInterface extends GenericGattModelInterface {

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
}
