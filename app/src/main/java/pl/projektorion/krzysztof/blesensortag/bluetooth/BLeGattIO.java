package pl.projektorion.krzysztof.blesensortag.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by krzysztof on 01.11.16.
 */

public interface BLeGattIO {
    public BluetoothGattService getService(UUID service);
    public boolean setNotificationEnable(BluetoothGattCharacteristic c, boolean state);
    public boolean writeCharacteristic(BluetoothGattCharacteristic c);
    public boolean writeDescriptor(BluetoothGattDescriptor d);
}
