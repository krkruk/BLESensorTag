package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.HeartRate;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.AppContext;
import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationDisableWriteCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationEnableWriteCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;

/**
 * Created by krzysztof on 01.02.17.
 */

public class HeartRateProfile implements NotifyGattProfileInterface {
    public static final UUID HEART_RATE_SERVICE =
            UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public static final UUID HEART_RATE_DATA =
            UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");

    private BLeGattIO gattClient;
    private boolean isNotifying;
    private double period = 1.0f;

    public HeartRateProfile(BLeGattIO gattClient) {
        this.gattClient = gattClient;
        this.isNotifying = false;
    }

    @Override
    public void enableNotification(boolean state) {
        if( isNotifying() == state ) return;
        isNotifying = state;

        BluetoothGattService service = gattClient.getService(HEART_RATE_SERVICE);
        if( service == null ) return;

        final BluetoothGattCharacteristic heartMeasurement = service
                .getCharacteristic(HEART_RATE_DATA);
        if( heartMeasurement == null ) return;

        if( state )
            gattClient.addWrite(new BLeNotificationEnableWriteCommand(gattClient, heartMeasurement));
        else
            gattClient.addWrite(new BLeNotificationDisableWriteCommand(gattClient, heartMeasurement));
    }

    @Override
    public boolean isNotifying() {
        return isNotifying;
    }

    @Override
    public void enableMeasurement(int state) {

    }

    @Override
    public int isMeasuring() {
        return DISABLE_ALL_MEASUREMENTS;
    }

    @Override
    public void configurePeriod(int input) {
    }

    @Override
    public double getPeriod() {
        return period;
    }

    @Override
    public UUID getDataUuid() {
        return HEART_RATE_DATA;
    }

    @Override
    public String getName() {
        return AppContext.getContext().getString(R.string.profile_heart_rate);
    }

    @Override
    public boolean isService(UUID serviceUuid) {
        return serviceUuid.equals(HEART_RATE_SERVICE);
    }
}
