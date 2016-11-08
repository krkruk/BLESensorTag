package pl.projektorion.krzysztof.blesensortag.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;


import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.utils.CommandAbstract;

/**
 * Created by krzysztof on 01.11.16.
 */

public interface BLeGattIO {
    /**
     * Add a command in order to write to the BLE device
     * @param cmd {@link CommandAbstract} A command executor method
     *                                   and a flag method
     */
    void addWrite(CommandAbstract cmd);


    /**
     * Add a command in order to read BLE characteristic.
     * @param cmd {@link CommandAbstract} A command executor method
     *                                   and a flag method
     */
    void addRead(CommandAbstract cmd);

    /**
     * Get {@link BluetoothGattService}
     * @param service UUID of the service to return
     * @return {@link BluetoothGattService}
     */
    BluetoothGattService getService(UUID service);

    /**
     * Low level, no synchronization access to write functionality to the BLE device.
     * @param c {@link BluetoothGattCharacteristic} to write
     * @param state True to enable notification. False otherwise
     * @return State whether demand is successful
     */
    boolean setNotificationEnable(BluetoothGattCharacteristic c, boolean state);

    /**
     * Low level, no synchronization access to write functionality to the BLE device.
     * @param c {@link BluetoothGattCharacteristic} to write
     * @return State whether demand is successful
     */
    boolean writeCharacteristic(BluetoothGattCharacteristic c);

    /**
     * Low level, no synchronization access to write functionality to the BLE device.
     * @param d {@link BluetoothGattDescriptor} to write
     * @return State whether demand is successful
     */
    boolean writeDescriptor(BluetoothGattDescriptor d);

    /**
     * Low level, no synchronization provided.
     * @param c {@link BluetoothGattCharacteristic} to be read
     * @return State whether demand is successful
     */
    boolean readCharacteristic(BluetoothGattCharacteristic c);

    /**
     * Low level, no synchronization provided.
     * @param d {@link BluetoothGattDescriptor} to be read
     * @return State whether demand is successful
     */
    boolean readDescriptor(BluetoothGattDescriptor d);
}
