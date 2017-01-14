package pl.projektorion.krzysztof.blesensortag.bluetooth.CustomProfile;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.abstracts.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.utils.ByteOperation;

/**
 * Created by krzysztof on 14.01.17.
 */

public class StethoscopeData extends AbstractProfileData {
    public static final int ATTRIBUTE_FIRST = 0x01;
    public static final int ATTRIBUTE_SECOND = 0x02;

    private double first = 0.0f;
    private double second = 0.0f;

    private static final short EXPECTED_ARRAY_SIZE = 4;

    public StethoscopeData() {
        super();
    }

    public StethoscopeData(byte[] data) {
        super(data);
        parse();
    }

    public StethoscopeData(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
        parse();
    }

    @Override
    protected void parse() {
        if( data.length != EXPECTED_ARRAY_SIZE ) {
            Log.e("StethoData", String.format(
                    "Array length: %d != expected %d",data.length, EXPECTED_ARRAY_SIZE));
            return;
        }

        final double SCALE_LSB = 0.03125f * 0.25f;
        byte[] ambientTemp = new byte[4];
        byte[] objectTemp = new byte[4];
        System.arraycopy(data, 0, ambientTemp, 2, 2);
        System.arraycopy(data, 2, objectTemp, 2, 2);

        first = ByteOperation.bytesToInt(ambientTemp) * SCALE_LSB;
        second = ByteOperation.bytesToInt(objectTemp) * SCALE_LSB;
    }

    @Override
    public double getValue(int sensorAttribute) {
        switch (sensorAttribute)
        {
            case ATTRIBUTE_FIRST:
                return first;
            case ATTRIBUTE_SECOND:
                return second;
            default:
                return -1.0f;
        }
    }
}
