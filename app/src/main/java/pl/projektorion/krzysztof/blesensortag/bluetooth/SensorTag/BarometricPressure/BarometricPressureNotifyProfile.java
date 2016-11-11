package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import android.bluetooth.BluetoothGattService;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.AbstractGenericGattNotifyProfile;
import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;

/**
 * Created by krzysztof on 03.11.16.
 */

public class BarometricPressureNotifyProfile extends AbstractGenericGattNotifyProfile {
    public static final UUID BAROMETRIC_PRESSURE_SERVICE =
            UUID.fromString("f000aa40-0451-4000-b000-000000000000");
    public static final UUID BAROMETRIC_PRESSURE_DATA =
            UUID.fromString("f000aa41-0451-4000-b000-000000000000");
    public static final UUID BAROMETRIC_PRESSURE_CONFIG =
            UUID.fromString("f000aa42-0451-4000-b000-000000000000");
    public static final UUID BAROMETRIC_PRESSURE_PERIOD =
            UUID.fromString("f000aa44-0451-4000-b000-000000000000");

    private static final String APP_NAME = ProfileName.BAROMETRIC_PRESSURE_PROFILE;

    public BarometricPressureNotifyProfile(BLeGattIO gattClient) {
        super(gattClient);
    }

    @Override
    public String getName() {
        return APP_NAME;
    }

    @Override
    protected BluetoothGattService get_service()
    {
        return gattClient.getService(BAROMETRIC_PRESSURE_SERVICE);
    }

    @Override
    protected UUID get_service_uuid() {
        return BAROMETRIC_PRESSURE_SERVICE;
    }

    @Override
    protected UUID get_data_uuid() {
        return BAROMETRIC_PRESSURE_DATA;
    }

    @Override
    protected UUID get_config_uuid() {
        return BAROMETRIC_PRESSURE_CONFIG;
    }

    @Override
    protected UUID get_period_uuid() {
        return BAROMETRIC_PRESSURE_PERIOD;
    }
}
