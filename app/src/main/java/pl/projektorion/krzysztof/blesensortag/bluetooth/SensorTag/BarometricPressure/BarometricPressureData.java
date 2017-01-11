package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;


import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.abstracts.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.utils.ByteOperation;


/**
 * Created by krzysztof on 03.11.16.
 */

public class BarometricPressureData extends AbstractProfileData {
    public static final int ATTRIBUTE_CENTIGRADE = 0x00;

    public static final int ATTRIBUTE_PRESSURE_hPa = 0x01;

    private static final int EXPECTED_ARRAY_SIZE = 6;
    private float pressure = 0;
    private float temperature = 0;

    public BarometricPressureData() {
        super();
    }

    public BarometricPressureData(byte[] data) {
        super(data);
        parse();
    }

    public BarometricPressureData(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
        parse();
        Log.i("Bar", String.format("Temp %f, Pressure: %f", temperature, pressure));
    }

    @Override
    public double getValue(int sensorAttribute) {
        switch (sensorAttribute)
        {
            case ATTRIBUTE_CENTIGRADE:
                return temperature;
            case ATTRIBUTE_PRESSURE_hPa:
                return pressure;
            default:
                return -1;
        }
    }

    @Override
    protected void parse() {
        if( data.length != EXPECTED_ARRAY_SIZE ) {
            Log.i("BPressData", String.format("Array length: %d",data.length));
            return;
        }

        byte[] bigEndianData = ByteOperation.littleToBigEndian(data);
        byte[] pressDat = new byte[4]; System.arraycopy(bigEndianData, 0, pressDat, 0, 3);
        byte[] tempDat = new byte[4]; System.arraycopy(bigEndianData, 3, tempDat, 0, 3);

        pressure = (ByteOperation.bytesToInt(pressDat)>>8) * 0.01f;
        temperature = (ByteOperation.bytesToInt(tempDat)>>8) * 0.01f;
    }
}
