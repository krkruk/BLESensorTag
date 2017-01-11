package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.util.Locale;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.abstracts.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.utils.ByteOperation;

/**
 * Created by krzysztof on 07.11.16.
 */

public class OpticalSensorData extends AbstractProfileData {
    public static final int ATTRIBUTE_LIGHT_INTENSITY_LUX = 0x01;

    private static final short EXPECTED_ARRAY_SIZE = 2;

    private double lightIntensity = 0.0f;

    public OpticalSensorData() {
        super();
    }

    public OpticalSensorData(byte[] data) {
        super(data);
        parse();
    }

    public OpticalSensorData(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
        parse();
        Log.i("OpticalSensor", String.format(Locale.ENGLISH,
                "%.02f LUX", lightIntensity));
    }

    @Override
    protected void parse() {
        if( data.length != EXPECTED_ARRAY_SIZE ) return;
        byte[] bigEndianData = ByteOperation.littleToBigEndian(data);
        short rawLightIntensity = ByteOperation.bytesToShort(bigEndianData);
        lightIntensity = compute_light_intensity(rawLightIntensity);
    }

    @Override
    public double getValue(int sensorAttribute) {
        switch (sensorAttribute)
        {
            case ATTRIBUTE_LIGHT_INTENSITY_LUX:
                return lightIntensity;
            default: return -1;
        }
    }

    private double compute_light_intensity(short rawLightIntensity)
    {
        int exponential, coefficient;
        coefficient = rawLightIntensity & 0x0fff;
        exponential =(rawLightIntensity & 0xf000) >> 12;
        return coefficient * (0.01f * Math.pow(2.0, exponential));
    }
}
