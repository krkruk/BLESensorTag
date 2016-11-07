package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by krzysztof on 03.11.16.
 */

public interface ProfileData {
    double getValue(int sensorAttribute);
    byte[] getRawData();
    void setValue(byte[] value);
    void setValue(BluetoothGattCharacteristic characteristic);
}
