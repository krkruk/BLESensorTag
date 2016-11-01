package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;

/**
 * Created by krzysztof on 01.11.16.
 */

public class SimpleKeysProfile {
    public static final UUID SIMPLE_KEY_SERVICE =
            UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID SIMPLE_KEY_DATA =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public static final UUID SIMPLE_KEY_DESCRIPTOR =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

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
        BluetoothGattCharacteristic notif = service.getCharacteristic(SIMPLE_KEY_DATA);
        gattClient.setNotificationEnable(notif, state);
        if(notif == null) return;
        BluetoothGattDescriptor enable = notif.getDescriptor(SIMPLE_KEY_DESCRIPTOR);
        enable.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        try{Thread.sleep(350);}catch (InterruptedException e) {return;}
        gattClient.writeDescriptor(enable);
    }

    private BluetoothGattService getService()
    {
        return gattClient.getService(SIMPLE_KEY_SERVICE);
    }
}
