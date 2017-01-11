package pl.projektorion.krzysztof.blesensortag.bluetooth.service;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * Created by krzysztof on 01.11.16.
 */

public interface BLeGattIOCallback {
    void onCharacteristicRead(BluetoothGatt gatt,
                              BluetoothGattCharacteristic characteristic, int status);
    void onCharacteristicWrite(BluetoothGatt gatt,
                               BluetoothGattCharacteristic characteristic, int status);
    void onCharacteristicChanged(BluetoothGatt gatt,
                                 BluetoothGattCharacteristic characteristic);
    void onDescriptorRead(BluetoothGatt gatt,
                          BluetoothGattDescriptor descriptor, int status);
    void onDescriptorWrite(BluetoothGatt gatt,
                           BluetoothGattDescriptor descriptor, int status);
}