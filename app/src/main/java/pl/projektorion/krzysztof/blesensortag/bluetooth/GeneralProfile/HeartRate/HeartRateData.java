package pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.HeartRate;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.abstracts.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.utils.ByteOperation;

/**
 * Created by krzysztof on 01.02.17.
 */

public class HeartRateData extends AbstractProfileData {

    public static final int ATTRIBUTE_HEART_RATE = 0x01;
    public static final int ATTRIBUTE_HEART_VALUE_FORMAT = 0x02;

    public static final int FORMAT_UINT8 = BluetoothGattCharacteristic.FORMAT_UINT8;
    public static final int FORMAT_UINT16 = BluetoothGattCharacteristic.FORMAT_UINT16;

    private static final byte HEART_RATE_VALUE_FORMAT_BIT = 0b1;
    private static final byte HEART_RATE_FORMAT_UINT8 = 0;
    private static final byte HEART_RATE_FORMAT_UINT16 = 1;

    private int heartRate = 0;
    private int valueFormat = FORMAT_UINT8;

    public HeartRateData() {
        super();
    }

    public HeartRateData(byte[] data) {
        super(data);
        parse();
    }

    public HeartRateData(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
        parse();
    }

    @Override
    public double getValue(int sensorAttribute) {
        switch (sensorAttribute)
        {
            case ATTRIBUTE_HEART_RATE:
                return heartRate;

            case ATTRIBUTE_HEART_VALUE_FORMAT:
                return valueFormat;

            default:
                return -1;
        }
    }

    @Override
    protected void parse() {
        Log.i("HEARTRATE", "data size: " + data.length);
        byte rawFormat = data[0];
        if( (rawFormat & HEART_RATE_VALUE_FORMAT_BIT) == HEART_RATE_FORMAT_UINT8 )
            valueFormat = FORMAT_UINT8;
        else if( (rawFormat & HEART_RATE_VALUE_FORMAT_BIT) == HEART_RATE_FORMAT_UINT16 )
            valueFormat = FORMAT_UINT16;
        else return;

        byte[] bigEndianData = ByteOperation.littleToBigEndian(data);
        byte[] heartRateData = new byte[4];
        if( valueFormat == FORMAT_UINT8 )
            System.arraycopy(bigEndianData, 0, heartRateData, 3, 1);
        if( valueFormat == FORMAT_UINT16 )
            System.arraycopy(bigEndianData, 0, heartRateData, 2, 2);

        heartRate = ByteOperation.bytesToInt(heartRateData);
        Log.i("HEARTRATE", "RATE: " + heartRate);
    }
}
