package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.utils.ByteOperation;

/**
 * Created by krzysztof on 07.11.16.
 */

public class MovementData extends AbstractProfileData {
    public static final int ATTRIBUTE_GYRO_Z = 0x00;
    public static final int ATTRIBUTE_GYRO_Y = 0x01;
    public static final int ATTRIBUTE_GYRO_X = 0x02;
    public static final int ATTRIBUTE_ACC_Z = 0x03;
    public static final int ATTRIBUTE_ACC_Y = 0x04;
    public static final int ATTRIBUTE_ACC_X = 0x05;
    public static final int ATTRIBUTE_MAGNET_Z = 0x06;
    public static final int ATTRIBUTE_MAGNET_Y = 0x07;
    public static final int ATTRIBUTE_MAGNET_X = 0x08;

    private static final int EXPECTED_ARRAY_SIZE = 18;
    private int accRange = 0;
    private float gyroZ = 0;
    private float gyroY = 0;
    private float gyroX = 0;
    private float accZ = 0;
    private float accY = 0;
    private float accX = 0;
    private float magnetZ = 0;
    private float magnetY = 0;
    private float magnetX = 0;

    public MovementData() {
        super();
    }

    public MovementData(byte[] data) {
        super(data);
        parse();
    }

    public MovementData(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
        parse();
    }

    @Override
    protected void parse() {
        if( data.length != EXPECTED_ARRAY_SIZE ) {
            Log.d("MovModel", "Received invalid array (incorrect length).");
            return;
        }

        /**
         * http://processors.wiki.ti.com/index.php/CC2650_SensorTag_User's_Guide#Movement_Sensor
         */
        byte[] bigEndianData = ByteOperation.littleToBigEndian(data);
        byte[] buffer;

        buffer = get_value_bytes(bigEndianData, 0, 2);
        magnetZ = ByteOperation.bytesToShort(buffer);
        buffer = get_value_bytes(bigEndianData, 2, 2);
        magnetY = ByteOperation.bytesToShort(buffer);
        buffer = get_value_bytes(bigEndianData, 4, 2);
        magnetZ = ByteOperation.bytesToShort(buffer);
        calculate_raw_magnet_data();

        buffer = get_value_bytes(bigEndianData, 6, 2);
        accZ = ByteOperation.bytesToShort(buffer);
        buffer = get_value_bytes(bigEndianData, 8, 2);
        accY = ByteOperation.bytesToShort(buffer);
        buffer = get_value_bytes(bigEndianData, 10, 2);
        accX = ByteOperation.bytesToShort(buffer);
        calculate_raw_acc_data();

        buffer = get_value_bytes(bigEndianData, 12, 2);
        gyroZ = ByteOperation.bytesToShort(buffer);
        buffer = get_value_bytes(bigEndianData, 14, 2);
        gyroY = ByteOperation.bytesToShort(buffer);
        buffer = get_value_bytes(bigEndianData, 16, 2);
        gyroX = ByteOperation.bytesToShort(buffer);
        calculate_raw_gyro_data();

        Log.i("Movement", String.format("GyrX:Y:Z - %f:%f:%f - AccX:Y:Z - %f:%f:%f - MagX:Y:Z - %f:%f:%f",
                gyroX, gyroY, gyroZ, accX, accY, accZ, magnetX, magnetY, magnetZ));
    }

    @Override
    public double getValue(int sensorAttribute) {
        switch (sensorAttribute)
        {
            case ATTRIBUTE_GYRO_Z:
                return gyroZ;
            case ATTRIBUTE_GYRO_Y:
                return gyroY;
            case ATTRIBUTE_GYRO_X:
                return gyroX;
            case ATTRIBUTE_ACC_Z:
                return accZ;
            case ATTRIBUTE_ACC_Y:
                return accY;
            case ATTRIBUTE_ACC_X:
                return accX;
            case ATTRIBUTE_MAGNET_Z:
                return magnetZ;
            case ATTRIBUTE_MAGNET_Y:
                return magnetY;
            case ATTRIBUTE_MAGNET_X:
                return magnetX;
            default: return -1.0f;
        }
    }

    /**
     * Set accelerometer range. See http://processors.wiki.ti.com/index.php/CC2650_SensorTag_User's_Guide#Configuration_2
     * Default value is {@link MovementProfile}::ACC_RANGE_2G
     * @param range {@link MovementProfile}
     * *ACC_RANGE_2G
     * *ACC_RANGE_4G
     * *ACC_RANGE_8G
     * *ACC_RANGE_16G
     */
    public void setAccelerometerRange(int range)
    {
        accRange = (range & MovementProfile.ACC_RANGE_CHECK_MASK);
    }

    /**
     * Subarray the data array. The client has to check whether the params
     * are correct. May throw an error if values are invalid.
     * @param src Data source
     * @param startAt Start point of {@param src} to be copied
     * @param length Array length to be copied.
     * @return Byte[] of size {@param length}
     */
    private byte[] get_value_bytes(byte[] src, int startAt, int length)
    {
        byte[] buffer = new byte[length];
        System.arraycopy(src, startAt, buffer, 0, length);
        return buffer;
    }

    private void calculate_raw_magnet_data()
    {
        magnetZ = magnet_convert(magnetZ);
        magnetY = magnet_convert(magnetY);
        magnetX = magnet_convert(magnetX);
    }
    protected float magnet_convert(float accValue)
    {
        return 1.0f * accValue;
    }

    private void calculate_raw_acc_data()
    {
        accZ = acc_convert(accZ);
        accY = acc_convert(accY);
        accX = acc_convert(accX);
    }

    protected float acc_convert(float accValue)
    {
        switch (accRange)
        {
            case MovementProfile.ACC_RANGE_2G:
                return (accValue * 1.0f) / (32_768.0f*0.5f);
            case MovementProfile.ACC_RANGE_4G:
                return (accValue * 1.0f) / (32_768.0f*0.25f);
            case MovementProfile.ACC_RANGE_8G:
                return (accValue * 1.0f) / (32_768.0f*0.125f);
            case MovementProfile.ACC_RANGE_16G:
                return (accValue * 1.0f) / (32_768.0f/16.0f);
            default: return -1.0f;
        }
    }

    private void calculate_raw_gyro_data()
    {
        gyroZ = gyro_convert(gyroZ);
        gyroY = gyro_convert(gyroY);
        gyroX = gyro_convert(gyroX);
    }

    protected float gyro_convert(float gyroValue)
    {
        return gyroValue / (65536 / 500 );
    }

}
