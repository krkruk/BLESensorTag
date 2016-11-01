package pl.projektorion.krzysztof.blesensortag.bluetooth.commands;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;

/**
 * Created by krzysztof on 01.11.16.
 */

public class BLeNotificationDisableWriteCommand extends BLeNotificationEnableWriteCommand {
    public BLeNotificationDisableWriteCommand(BLeGattIO gattClient,
                                              BluetoothGattCharacteristic dataCharacteristic) {
        super(gattClient, dataCharacteristic);
        set_notification_value(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        set_state(false);
    }
}
