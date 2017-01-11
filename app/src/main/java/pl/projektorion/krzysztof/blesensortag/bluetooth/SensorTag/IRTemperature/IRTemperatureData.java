package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.util.Locale;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.abstracts.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.utils.ByteOperation;

/**
 * Created by krzysztof on 06.11.16.
 */

public class IRTemperatureData extends AbstractProfileData {
    public static final int ATTRIBUTE_AMBIENT_TEMPERATURE = 0x00;
    public static final int ATTRIBUTE_OBJECT_TEMPERATURE = 0x01;

    private static final short EXPECTED_ARRAY_SIZE = 4;

    private double temperatureAmbient = 0.0f;
    private double temperatureObject = 0.0f;


    public IRTemperatureData() {
        super();
    }

    public IRTemperatureData(byte[] data) {
        super(data);
        parse();
    }

    public IRTemperatureData(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
        parse();
        Log.i("IRTemp", String.format(Locale.ENGLISH, "Ambient: %f, Object: %f", temperatureAmbient, temperatureObject));
    }

    @Override
    protected void parse() {
        if( data.length != EXPECTED_ARRAY_SIZE ) {
            Log.i("IRTempData", String.format(
                    "Array length: %d != expected %d",data.length, EXPECTED_ARRAY_SIZE));
            return;
        }

        final double SCALE_LSB = 0.03125f * 0.25f;
        byte[] ambientTemp = new byte[4];
        byte[] objectTemp = new byte[4];
        System.arraycopy(data, 0, ambientTemp, 2, 2);
        System.arraycopy(data, 2, objectTemp, 2, 2);

        temperatureAmbient = ByteOperation.bytesToInt(ambientTemp) * SCALE_LSB;
        temperatureObject = ByteOperation.bytesToInt(objectTemp) * SCALE_LSB;
    }

    @Override
    public double getValue(int sensorAttribute) {
        switch (sensorAttribute)
        {
            case ATTRIBUTE_AMBIENT_TEMPERATURE:
                return temperatureAmbient;
            case ATTRIBUTE_OBJECT_TEMPERATURE:
                return temperatureObject;
            default: return -1;
        }
    }
}
