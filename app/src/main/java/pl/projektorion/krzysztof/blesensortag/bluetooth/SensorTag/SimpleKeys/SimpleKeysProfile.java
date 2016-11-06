package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.SimpleKeys;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationDisableWriteCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationEnableWriteCommand;
import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;


/**
 * Created by krzysztof on 01.11.16.
 */

public class SimpleKeysProfile
    implements GenericGattProfileInterface {
    public static final UUID SIMPLE_KEY_SERVICE =
            UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID SIMPLE_KEY_DATA =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    private static final String APP_NAME = ProfileName.SIMPLE_KEY_PROFILE;

    private BLeGattIO gattClient;
    private BluetoothGattService service;

    private boolean isNotifying;


    public SimpleKeysProfile(BLeGattIO gattClient) {
        this.gattClient = gattClient;
        init();
    }

    @Override
    public void enableNotification(boolean state) {
        if (isNotifying == state) return;
        isNotifying = state;

        service = getService();
        if (service == null) return;
        final BluetoothGattCharacteristic notify = service.getCharacteristic(SIMPLE_KEY_DATA);

        if (state)
            gattClient.add(new BLeNotificationEnableWriteCommand(gattClient, notify));
        else
            gattClient.add(new BLeNotificationDisableWriteCommand(gattClient, notify));
    }

    /**
     * Empty.
     *
     * @param state Does not concern.
     */
    @Override
    public void enableMeasurement(boolean state) {}

    /**
     * Empty. React to an event.
     *
     * @param input Does not concern.
     */
    @Override
    public void configurePeriod(byte input) {}

    @Override
    public String getName() {
        return APP_NAME;
    }

    private void init()
    {
        this.isNotifying = false;
        this.service = getService();
    }

    private BluetoothGattService getService()
    {
        return gattClient.getService(SIMPLE_KEY_SERVICE);
    }
}
