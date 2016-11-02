package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.Locale;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattObserverInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationDisableWriteCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationEnableWriteCommand;


/**
 * Created by krzysztof on 01.11.16.
 */

public class SimpleKeysProfile
    implements GenericGattProfileInterface, GenericGattObserverInterface {
    public static final UUID SIMPLE_KEY_SERVICE =
            UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID SIMPLE_KEY_DATA =
            UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");



    private BLeGattIO gattClient;
    private BluetoothGattService service;

    private boolean isNotifying;
    private SimpleKeysData data;

    public SimpleKeysProfile(BLeGattIO gattClient) {
        this.gattClient = gattClient;
        this.isNotifying = false;
        this.service = getService();
        this.data = new SimpleKeysData();
    }

    @Override
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

    /**
     * Empty.
     * @param state Does not concern.
     */
    @Override
    public void enableMeasurement(boolean state) {}

    /**
     * Empty. React to an event.
     * @param input Does not concern.
     */
    @Override
    public void configurePeriod(int input) {}

    @Override
    public byte[] getRawData() {
        return data.getRawData();
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public UUID getDataUuid() {
        return SIMPLE_KEY_DATA;
    }

    @Override
    public void updateCharacteristic(BluetoothGattCharacteristic characteristic) {
        data = new SimpleKeysData(characteristic);
        final int leftButton = data.getValue(SimpleKeysData.ATTRIBUTE_LEFT_BUTTON);
        final int rightButton = data.getValue(SimpleKeysData.ATTRIBUTE_RIGHT_BUTTON);
        final int reedRelay = data.getValue(SimpleKeysData.ATTRIBUTE_REED_RELAY);

        Log.i("Keys", String.format(Locale.ENGLISH, "LeftBTN: %d, RightBTN: %d, ReedRelay: %d",
                leftButton, rightButton, reedRelay));
    }

    private BluetoothGattService getService()
    {
        return gattClient.getService(SIMPLE_KEY_SERVICE);
    }
}
