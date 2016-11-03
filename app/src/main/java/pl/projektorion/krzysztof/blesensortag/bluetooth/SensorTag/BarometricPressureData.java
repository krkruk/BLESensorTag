package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;


/**
 * Created by krzysztof on 03.11.16.
 */

public class BarometricPressureData extends AbstractProfileData {
    private static final int EXPECTED_ARRAY_SIZE = 6;
    private int pressure = 0;
    private int temperature = 0;

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
        Log.i("Bar", String.format("Temp %d, Pressure: %d", temperature, pressure));
    }

    @Override
    public int getValue(int sensorAttribute) {
        return 0;
    }

    @Override
    protected void parse() {
        if( data.length != EXPECTED_ARRAY_SIZE ) {
            Log.i("BPressData", String.format("Array length: %d",data.length));
            return;
        }

        pressure = data[5]<<2 | data[4]<<1 | data[3];
        temperature = data[2]<<2 | data[1]<<1 | data[0];

        pressure *= 0.01; temperature *= 0.01;
    }
}
