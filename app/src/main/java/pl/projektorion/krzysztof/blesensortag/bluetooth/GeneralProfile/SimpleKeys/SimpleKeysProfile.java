package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.SimpleKeys;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationDisableWriteCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationEnableWriteCommand;
import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;


/**
 * Created by krzysztof on 01.11.16.
 */

public class SimpleKeysProfile
    implements NotifyGattProfileInterface {
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
            gattClient.addWrite(new BLeNotificationEnableWriteCommand(gattClient, notify));
        else
            gattClient.addWrite(new BLeNotificationDisableWriteCommand(gattClient, notify));
    }

    @Override
    public boolean isNotifying() {
        return isNotifying;
    }

    /**
     * Empty.
     *
     * @param state Does not concern.
     */
    @Override
    public void enableMeasurement(int state) {}

    @Override
    public int isMeasuring() {
        return -1;
    }

    /**
     * Empty. React to an event.
     *
     * @param input Does not concern.
     */
    @Override
    public void configurePeriod(int input) {}

    /**
     * Does not concern.
     * @return integer 0
     */
    @Override
    public int getPeriod() {
        return 0;
    }

    @Override
    public String getName() {
        return APP_NAME;
    }

    @Override
    public boolean isService(UUID serviceUuid) {
        return SIMPLE_KEY_SERVICE.equals(serviceUuid);
    }

    @Override
    public UUID getDataUuid() {
        return SIMPLE_KEY_DATA;
    }

    private void init()
    {
        this.isNotifying = false;
    }

    private BluetoothGattService getService()
    {
        return gattClient.getService(SIMPLE_KEY_SERVICE);
    }
}
