package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattProfile;
import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;

/**
 * Created by krzysztof on 07.11.16.
 */

public class HumidityProfile extends AbstractGenericGattProfile {
    public static final UUID HUMIDITY_SERVICE =
            UUID.fromString("f000aa20-0451-4000-b000-000000000000");
    public static final UUID HUMIDITY_DATA =
            UUID.fromString("f000aa21-0451-4000-b000-000000000000");
    public static final UUID HUMIDITY_CONFIG =
            UUID.fromString("f000aa22-0451-4000-b000-000000000000");
    public static final UUID HUMIDITY_PERIOD =
            UUID.fromString("f000aa23-0451-4000-b000-000000000000");

    private static final String APP_NAME = ProfileName.HUMIDITY_PROFILE;

    public HumidityProfile(BLeGattIO gattClient) {
        super(gattClient);
    }

    @Override
    protected BluetoothGattService get_service() {
        return gattClient.getService(HUMIDITY_SERVICE);
    }

    @Override
    protected UUID get_data_uuid() {
        return HUMIDITY_DATA;
    }

    @Override
    protected UUID get_config_uuid() {
        return HUMIDITY_CONFIG;
    }

    @Override
    protected UUID get_period_uuid() {
        return HUMIDITY_PERIOD;
    }

    @Override
    public String getName() {
        return APP_NAME;
    }
}
