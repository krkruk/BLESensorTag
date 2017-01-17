package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import android.bluetooth.BluetoothGattService;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.AppContext;
import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.abstracts.AbstractNotifyGattProfile;

/**
 * Created by krzysztof on 03.11.16.
 */

public class BarometricPressureProfile extends AbstractNotifyGattProfile {
    public static final UUID BAROMETRIC_PRESSURE_SERVICE =
            UUID.fromString("f000aa40-0451-4000-b000-000000000000");
    public static final UUID BAROMETRIC_PRESSURE_DATA =
            UUID.fromString("f000aa41-0451-4000-b000-000000000000");
    public static final UUID BAROMETRIC_PRESSURE_CONFIG =
            UUID.fromString("f000aa42-0451-4000-b000-000000000000");
    public static final UUID BAROMETRIC_PRESSURE_PERIOD =
            UUID.fromString("f000aa44-0451-4000-b000-000000000000");


    public BarometricPressureProfile(BLeGattIO gattClient) {
        super(gattClient, 1000.0f);
    }

    @Override
    public String getName() {
        return AppContext.getContext().getString(R.string.profile_barometric_pressure);
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
