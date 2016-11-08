package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.AbstractGenericGattNotifyProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.commands.BLeCharacteristicWriteCommand;
import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;
import pl.projektorion.krzysztof.blesensortag.utils.ByteOperation;

/**
 * Created by krzysztof on 06.11.16.
 */

public class MovementNotifyProfile extends AbstractGenericGattNotifyProfile {
    public static final UUID MOVEMENT_SERVICE =
            UUID.fromString("f000aa80-0451-4000-b000-000000000000");
    public static final UUID MOVEMENT_DATA =
            UUID.fromString("f000aa81-0451-4000-b000-000000000000");
    public static final UUID MOVEMENT_CONFIG =
            UUID.fromString("f000aa82-0451-4000-b000-000000000000");
    public static final UUID MOVEMENT_PERIOD =
            UUID.fromString("f000aa83-0451-4000-b000-000000000000");

    public static final int ENABLE_GYRO_Z = 0b0000000100000000;     //All values in Little Endian Order
    public static final int ENABLE_GYRO_Y = 0b0000001000000000;
    public static final int ENABLE_GYRO_X = 0b0000010000000000;
    public static final int ENABLE_ACC_Z =  0b0000100000000000;
    public static final int ENABLE_ACC_Y =  0b0001000000000000;
    public static final int ENABLE_ACC_X =          0b0010000000000000;
    public static final int ENABLE_MAGNETOMETER =   0b0100000000000000;
    public static final int ENABLE_WAKE_ON_MOTION = 0b1000000000000000;
    public static final int ACC_RANGE_2G = 0b0000000000000000;
    public static final int ACC_RANGE_4G = 0b0000_0000_1000_0000;
    public static final int ACC_RANGE_8G = 0b0000_0000_0100_0000;
    public static final int ACC_RANGE_16G = 0b0000_0000_1100_0000;
    public static final int ACC_RANGE_CHECK_MASK = 0b1100_0000;

    private static final String APP_NAME = ProfileName.MOVEMENT_PROFILE;
    private int enableMeasurementStatus = DISABLE_ALL_MEASUREMENTS;

    public MovementNotifyProfile(BLeGattIO gattClient) {
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
        if( enableMeasurementStatus == state ) return;
        enableMeasurementStatus = state;
        BluetoothGattService service = get_service();
        if( service == null ) return;

        byte[] cmd = get_measurement_cmd(state);
        if( state == DISABLE_ALL_MEASUREMENTS ) cmd = new byte[] {0x00, 0x00};
        BluetoothGattCharacteristic measurement = service.getCharacteristic(get_config_uuid());
        measurement.setValue(cmd);

        gattClient.addWrite(new BLeCharacteristicWriteCommand(gattClient, measurement));
    }

    @Override
    public int isMeasuring() {
        return enableMeasurementStatus;
    }

    private byte[] get_measurement_cmd(int cmd)
    {
        byte[] dataInt = ByteOperation.intToBytes(cmd);
        byte[] cmdSensorTag = new byte[2];
        System.arraycopy(dataInt, 2, cmdSensorTag, 0, 2);
        return cmdSensorTag;
    }
}
