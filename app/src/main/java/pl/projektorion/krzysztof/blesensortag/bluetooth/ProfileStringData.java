package pl.projektorion.krzysztof.blesensortag.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by krzysztof on 08.11.16.
 */

public interface ProfileStringData {
    String getValue(int dataAttribute);
    void setValue(BluetoothGattCharacteristic characteristic);
}
