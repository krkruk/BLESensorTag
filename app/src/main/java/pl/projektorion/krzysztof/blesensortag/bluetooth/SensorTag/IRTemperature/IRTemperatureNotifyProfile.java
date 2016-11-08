package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature;

import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractGenericGattNotifyProfile;
import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;

/**
 * Created by krzysztof on 06.11.16.
 */

public class IRTemperatureNotifyProfile extends AbstractGenericGattNotifyProfile {
    public static final UUID IR_TEMPERATURE_SERVICE =
            UUID.fromString("f000aa00-0451-4000-b000-000000000000");
    public static final UUID IR_TEMPERATURE_DATA =
            UUID.fromString("f000aa01-0451-4000-b000-000000000000");
    public static final UUID IR_TEMPERATURE_CONFIG =
            UUID.fromString("f000aa02-0451-4000-b000-000000000000");
    public static final UUID IR_TEMPERATURE_PERIOD =
            UUID.fromString("f000aa03-0451-4000-b000-000000000000");

    private static final String APP_NAME = ProfileName.IR_TEMPERATURE_PROFILE;

    public IRTemperatureNotifyProfile(BLeGattIO gattClient) {
        super(gattClient);
    }

    @Override
    public void configurePeriod(byte input) {
        if( input < (byte) 0x1e) return;
        super.configurePeriod(input);
    }

    @Override
    protected BluetoothGattService get_service() {
        return gattClient.getService(IR_TEMPERATURE_SERVICE);
    }

    @Override
    protected UUID get_data_uuid() {
        return IR_TEMPERATURE_DATA;
    }

    @Override
    protected UUID get_config_uuid() {
        return IR_TEMPERATURE_CONFIG;
    }

    @Override
    protected UUID get_period_uuid() {
        return IR_TEMPERATURE_PERIOD;
    }

    @Override
    public String getName() {
        return APP_NAME;
    }
}
