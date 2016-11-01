package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeDescriptorWriteCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationEnableCommand;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;


/**
 * Created by krzysztof on 01.11.16.
 */

public class SimpleKeysProfile {
    public static final UUID SIMPLE_KEY_SERVICE =
            UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID SIMPLE_KEY_DATA =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    private BLeGattIO gattClient;
    private BluetoothGattService service;

    public SimpleKeysProfile(BLeGattIO gattClient) {
        this.gattClient = gattClient;

        service = getService();
    }

    public void enableNotification(boolean state)
    {
        service = getService();
        if(service == null) return;
        BluetoothGattCharacteristic notify = service.getCharacteristic(SIMPLE_KEY_DATA);
        if(notify == null) return;
        BluetoothGattDescriptor enable = notify.getDescriptor(
                Constant.CLIENT_CHARACTERISTIC_CONFIGURATION_UUID);
        enable.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        gattClient.add(new BLeNotificationEnableCommand(gattClient, notify, state));
        gattClient.add(new BLeDescriptorWriteCommand(gattClient, enable));
    }

    private BluetoothGattService getService()
    {
        return gattClient.getService(SIMPLE_KEY_SERVICE);
    }
}
