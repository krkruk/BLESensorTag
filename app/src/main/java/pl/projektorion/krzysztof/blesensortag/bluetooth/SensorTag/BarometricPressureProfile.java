package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeCharacteristicWriteCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationDisableWriteCommand;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeNotificationEnableWriteCommand;
import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;

/**
 * Created by krzysztof on 03.11.16.
 */

public class BarometricPressureProfile implements GenericGattProfileInterface {
    public static final UUID BAROMETRIC_PRESSURE_SERVICE =
            UUID.fromString("f000aa40-0451-4000-b000-000000000000");
    public static final UUID BAROMETRIC_PRESSURE_DATA =
            UUID.fromString("f000aa41-0451-4000-b000-000000000000");
    public static final UUID BAROMETRIC_PRESSURE_CONFIG =
            UUID.fromString("f000aa42-0451-4000-b000-000000000000");
    public static final UUID BAROMETRIC_PRESSURE_PERIOD =
            UUID.fromString("f000aa44-0451-4000-b000-000000000000");

    private static final String APP_NAME = ProfileName.BAROMETRIC_PRESSURE_PROFILE;
    private static final byte[] CONFIG_ENABLE = {0x01};
    private static final byte[] CONFIG_DISABLE = {0x00};

    private BLeGattIO gattClient;
    private BluetoothGattService service;

    private boolean isNotifying;
    private boolean isMeasuring;

    public BarometricPressureProfile(BLeGattIO gattClient) {
        this.gattClient = gattClient;
        this.service = getService();
        this.isNotifying = false;
        this.isMeasuring = false;
    }

    @Override
    public void enableNotification(boolean state) {
        if (isNotifying == state) return;
        isNotifying = state;

        service = getService();
        if (service == null) return;
        final BluetoothGattCharacteristic notify = service.getCharacteristic(BAROMETRIC_PRESSURE_DATA);

        if (state)
            gattClient.add(new BLeNotificationEnableWriteCommand(gattClient, notify));
        else
            gattClient.add(new BLeNotificationDisableWriteCommand(gattClient, notify));
    }

    @Override
    public void enableMeasurement(boolean state) {
        if( isMeasuring == state ) return;
        isMeasuring = state;

        service = getService();
        if( service == null ) return;

        final byte[] cmd = state ? CONFIG_ENABLE : CONFIG_DISABLE;
        final BluetoothGattCharacteristic measure = service.getCharacteristic(BAROMETRIC_PRESSURE_CONFIG);
        measure.setValue(cmd);
        gattClient.add(new BLeCharacteristicWriteCommand(gattClient, measure));
    }

    @Override
    public void configurePeriod(byte input) {
        Log.i("Config", "Period configured started");
        service = getService();
        if( service == null ) return;
        if( input < (byte) 10) return;

        final BluetoothGattCharacteristic period = service.getCharacteristic(BAROMETRIC_PRESSURE_PERIOD);
        period.setValue(new byte[] { input });
        gattClient.add(new BLeCharacteristicWriteCommand(gattClient, period));
        Log.i("Config", "Period configured");
    }

    @Override
    public String getName() {
        return APP_NAME;
    }

    private BluetoothGattService getService()
    {
        return gattClient.getService(BAROMETRIC_PRESSURE_SERVICE);
    }
}
