package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.AbstractGenericGattProfile;
import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;

/**
 * Created by krzysztof on 06.11.16.
 */

public class MovementProfile extends AbstractGenericGattProfile {
    public static final UUID MOVEMENT_SERVICE =
            UUID.fromString("f000aa80-0451-4000-b000-000000000000");
    public static final UUID MOVEMENT_DATA =
            UUID.fromString("f000aa81-0451-4000-b000-000000000000");
    public static final UUID MOVEMENT_CONFIG =
            UUID.fromString("f000aa82-0451-4000-b000-000000000000");
    public static final UUID MOVEMENT_PERIOD =
            UUID.fromString("f000aa83-0451-4000-b000-000000000000");

    public static final int ENABLE_GYRO_Z = 0b1000000000000000;     //All values in Little Endian Order
    public static final int ENABLE_GYRO_Y = 0b0100000000000000;
    public static final int ENABLE_GYRO_X = 0b0010000000000000;
    public static final int ENABLE_ACC_Z =  0b0001000000000000;
    public static final int ENABLE_ACC_Y =  0b0000100000000000;
    public static final int ENABLE_ACC_X =          0b0000010000000000;
    public static final int ENABLE_MAGNETOMETER =   0b0000001000000000;
    public static final int ENABLE_WAKE_ON_MOTION = 0b0000000100000000;
    public static final int ACC_RANGE_2G = 0b0000000000000000;
    public static final int ACC_RANGE_4G = 0b0000000000000001;
    public static final int ACC_RANGE_8G = 0b0000000000000010;
    public static final int ACC_RANGE_16G = 0b0000000000000011;
    public static final int ACC_RANGE_CHECK_MASK = 0b11;

    private static final String APP_NAME = ProfileName.MOVEMENT_PROFILE;

    public MovementProfile(BLeGattIO gattClient) {
        super(gattClient);
    }

    @Override
    protected BluetoothGattService get_service() {
        return gattClient.getService(MOVEMENT_SERVICE);
    }

    @Override
    protected UUID get_data_uuid() {
        return MOVEMENT_DATA;
    }

    @Override
    protected UUID get_config_uuid() {
        return MOVEMENT_CONFIG;
    }

    @Override
    protected UUID get_period_uuid() {
        return MOVEMENT_PERIOD;
    }

    @Override
    public String getName() {
        return APP_NAME;
    }

    @Override
    public void enableMeasurement(int state) {
        super.enableMeasurement(state);
    }
}
