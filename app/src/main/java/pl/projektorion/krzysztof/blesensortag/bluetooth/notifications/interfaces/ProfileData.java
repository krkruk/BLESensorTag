package pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by krzysztof on 03.11.16.
 */

public interface ProfileData {

    /**
     * Get data extracted from {@link BluetoothGattCharacteristic}.
     * @param sensorAttribute integer - attribute that allows selecting a proper value
     *                        to return
     * @return double - expected value
     */
    double getValue(int sensorAttribute);

    /**
     * Return raw data as a byte array
     * @return Raw data - little endian order
     */
    byte[] getRawData();

    /**
     * Set value.
     * @param value - Raw value in little endian order
     */
    void setValue(byte[] value);

    /**
     * Set value
     * @param characteristic - The objects extracts raw data itself
     *                       from {@link BluetoothGattCharacteristic}
     */
    void setValue(BluetoothGattCharacteristic characteristic);
}
