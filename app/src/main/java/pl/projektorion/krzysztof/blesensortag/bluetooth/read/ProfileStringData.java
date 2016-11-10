package pl.projektorion.krzysztof.blesensortag.bluetooth.read;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by krzysztof on 08.11.16.
 */

public interface ProfileStringData {
    /**
     * Get string value received from the BLE device
     * @param dataAttribute int - attribute you want to select
     * @return String value
     */
    String getValue(int dataAttribute);

    /**
     * Set characteristic that contains data to be parsed and
     * retrieved.
     * @param characteristic {@link BluetoothGattCharacteristic} containing data
     */
    void setValue(BluetoothGattCharacteristic characteristic);
}
