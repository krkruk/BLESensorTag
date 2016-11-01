package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationDisableWriteCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationEnableWriteCommand;


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
    private boolean isNotifying;

    public SimpleKeysProfile(BLeGattIO gattClient) {
        this.gattClient = gattClient;
        this.isNotifying = false;
        service = getService();
    }

    public void enableNotification(boolean state)
    {
        if( isNotifying == state ) return;
        isNotifying = state;

        service = getService();
        if(service == null) return;
        final BluetoothGattCharacteristic notify = service.getCharacteristic(SIMPLE_KEY_DATA);

        if( state )
            gattClient.add(new BLeNotificationEnableWriteCommand(gattClient, notify));
        else
            gattClient.add(new BLeNotificationDisableWriteCommand(gattClient, notify));
    }

    private BluetoothGattService getService()
    {
        return gattClient.getService(SIMPLE_KEY_SERVICE);
    }
}
