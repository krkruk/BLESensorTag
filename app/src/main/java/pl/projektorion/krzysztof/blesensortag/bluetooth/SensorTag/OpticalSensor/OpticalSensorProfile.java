package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor;

import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.AbstractGenericGattProfile;
import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;

/**
 * Created by krzysztof on 07.11.16.
 */

public class OpticalSensorProfile extends AbstractGenericGattProfile {
    public static final UUID OPTICAL_SENSOR_SERVICE =
            UUID.fromString("f000aa70-0451-4000-b000-000000000000");
    public static final UUID OPTICAL_SENSOR_DATA =
            UUID.fromString("f000aa71-0451-4000-b000-000000000000");
    public static final UUID OPTICAL_SENSOR_CONFIG =
            UUID.fromString("f000aa72-0451-4000-b000-000000000000");
    public static final UUID OPTICAL_SENSOR_PERIOD =
            UUID.fromString("f000aa73-0451-4000-b000-000000000000");

    private static final String APP_NAME = ProfileName.OPTICAL_SENSOR_PROFILE;

    public OpticalSensorProfile(BLeGattIO gattClient) {
        super(gattClient);
    }

    @Override
    protected BluetoothGattService get_service() {
        return gattClient.getService(OPTICAL_SENSOR_SERVICE);
    }

    @Override
    protected UUID get_data_uuid() {
        return OPTICAL_SENSOR_DATA;
    }

    @Override
    protected UUID get_config_uuid() {
        return OPTICAL_SENSOR_CONFIG;
    }

    @Override
    protected UUID get_period_uuid() {
        return OPTICAL_SENSOR_PERIOD;
    }

    @Override
    public String getName() {
        return APP_NAME;
    }
}
