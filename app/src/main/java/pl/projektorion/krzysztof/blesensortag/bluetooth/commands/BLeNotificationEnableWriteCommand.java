package pl.projektorion.krzysztof.blesensortag.bluetooth.commands;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.utils.CommandAbstract;

/**
 * Created by krzysztof on 01.11.16.
 */

public class BLeNotificationEnableWriteCommand extends CommandAbstract {
    private BLeGattIO gattClient;
    private BluetoothGattCharacteristic dataCharacteristic;
    private byte[] notificationValue= BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
    private boolean state = true;

    public BLeNotificationEnableWriteCommand(BLeGattIO gattClient,
                                             BluetoothGattCharacteristic dataCharacteristic) {
        this.gattClient = gattClient;
        this.dataCharacteristic = dataCharacteristic;
    }

    @Override
    public void execute() {
        if(dataCharacteristic == null) return;

        BluetoothGattDescriptor enable = dataCharacteristic.getDescriptor(
                Constant.CLIENT_CHARACTERISTIC_CONFIGURATION_UUID);
        enable.setValue(notificationValue);

        gattClient.addWrite(new BLeNotificationSettingCommand(gattClient, dataCharacteristic, state));
        gattClient.addWrite(new BLeDescriptorWriteCommand(gattClient, enable));
    }

    @Override
    public boolean autoContinue() {
        return true;
    }

    protected void set_notification_value(byte[] notification_value)
    {
        this.notificationValue = notification_value;
    }

    protected void set_state(boolean state) {
        this.state = state;
    }
}
