package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature;

import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.AppContext;
import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.abstracts.AbstractNotifyGattProfile;

/**
 * Created by krzysztof on 06.11.16.
 */

public class IRTemperatureProfile extends AbstractNotifyGattProfile {
    public static final UUID IR_TEMPERATURE_SERVICE =
            UUID.fromString("f000aa00-0451-4000-b000-000000000000");
    public static final UUID IR_TEMPERATURE_DATA =
            UUID.fromString("f000aa01-0451-4000-b000-000000000000");
    public static final UUID IR_TEMPERATURE_CONFIG =
            UUID.fromString("f000aa02-0451-4000-b000-000000000000");
    public static final UUID IR_TEMPERATURE_PERIOD =
            UUID.fromString("f000aa03-0451-4000-b000-000000000000");

    public IRTemperatureProfile(BLeGattIO gattClient) {
        super(gattClient, 1000.0f);
    }

    @Override
    public void configurePeriod(int input) {
        if( input < (byte) 0x1e) input = 0x1e;
        super.configurePeriod(input);
    }

    @Override
    protected BluetoothGattService get_service() {
        return gattClient.getService(IR_TEMPERATURE_SERVICE);
    }

    @Override
    protected UUID get_service_uuid() {
        return IR_TEMPERATURE_SERVICE;
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
        return AppContext.getContext().getString(R.string.profile_ir_temperature);
    }
}
