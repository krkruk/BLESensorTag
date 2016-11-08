package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.util.Locale;

import pl.projektorion.krzysztof.blesensortag.bluetooth.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.utils.ByteOperation;

/**
 * Created by krzysztof on 07.11.16.
 */

public class HumidityData extends AbstractProfileData {

    public static final int ATTRIBUTE_TEMPERATURE_CELSIUS = 0x00;
    public static final int ATTRIBUTE_RELATIVE_HUMIDITY = 0x01;
    private static final int EXPECTED_ARRAY_SIZE = 4;

    private double temperature = 0.0f;
    private double relativeHumidity = 0.0f;

    public HumidityData() {
        super();
    }

    public HumidityData(byte[] data) {
        super(data);
        parse();
    }

    public HumidityData(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
        parse();
        Log.i("Humidity", String.format(Locale.ENGLISH,
                "Humidity: %.02f, Temperature: %.02f", relativeHumidity, temperature));
    }

    @Override
    protected void parse() {
        if( data.length != EXPECTED_ARRAY_SIZE ) return;
        final short SHORT_SIZE = 2;

        byte[] bigEndianData = ByteOperation.littleToBigEndian(data);
        byte[] temp = new byte[SHORT_SIZE];
        System.arraycopy(bigEndianData, 0, temp, 0, SHORT_SIZE);
        short tempValue = ByteOperation.bytesToShort(temp);
        relativeHumidity = convert_relative_humidity(tempValue);

        System.arraycopy(bigEndianData, 2, temp, 0, SHORT_SIZE);
        tempValue = ByteOperation.bytesToShort(temp);
        temperature = convert_temperature(tempValue);
    }

    @Override
    public double getValue(int sensorAttribute) {
        switch (sensorAttribute)
        {
            case ATTRIBUTE_TEMPERATURE_CELSIUS:
                return temperature;
            case ATTRIBUTE_RELATIVE_HUMIDITY:
                return relativeHumidity;
            default: return -1;
        }
    }

    protected double convert_temperature(short rawTemperature)
    {
        return ( (double) rawTemperature * 165.0f ) / 65536.0f - 40.0f;
    }

    protected double convert_relative_humidity(short rawHumidity)
    {
        return ( (double) rawHumidity * 100.0f ) / 65536.0f;
    }
}
