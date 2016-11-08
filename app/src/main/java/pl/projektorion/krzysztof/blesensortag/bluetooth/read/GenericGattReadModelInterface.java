package pl.projektorion.krzysztof.blesensortag.bluetooth.read;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;


/**
 * Created by krzysztof on 08.11.16.
 */

public interface GenericGattReadModelInterface {
    void updateCharacteristic(BluetoothGattCharacteristic characteristic);
    boolean hasCharacteristic(BluetoothGattCharacteristic characteristic);
    Object getData();
}
