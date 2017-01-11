package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.abstracts.AbstractProfileData;
import pl.projektorion.krzysztof.blesensortag.utils.ByteOperation;

/**
 * Created by krzysztof on 10.11.16.
 */

public class ConnectionControlData extends AbstractProfileData {
    public static final int ATTRIBUTE_CONNECTION_INTERVAL = 0x00;
    public static final int ATTRIBUTE_SLAVE_LATENCY = 0x01;
    public static final int ATTRIBUTE_SUPERVISION_TIMEOUT = 0x02;

    private int connectionInterval = 0;
    private int slaveLatency = 0;
    private int supervisionTimeout = 0;

    public ConnectionControlData() {
        super();
    }

    public ConnectionControlData(byte[] data) {
        super(data);
        parse();
    }

    public ConnectionControlData(BluetoothGattCharacteristic characteristic) {
        super(characteristic);
        parse();
    }

    @Override
    protected void parse() {
        final byte[] bigEndian = ByteOperation.littleToBigEndian(data);
        final int BUFFER_SIZE = 2;
        byte[] buffer = new byte[BUFFER_SIZE];

        System.arraycopy(bigEndian, 0, buffer, 0, BUFFER_SIZE);
        supervisionTimeout = ByteOperation.bytesToShort(buffer);

        System.arraycopy(bigEndian, 2, buffer, 0, BUFFER_SIZE);
        slaveLatency = ByteOperation.bytesToShort(buffer);

        System.arraycopy(bigEndian, 4, buffer, 0, BUFFER_SIZE);
        connectionInterval = ByteOperation.bytesToShort(buffer);

        Log.i("CCD", String.format("ConnInterval: %d, Slave latency: %d, Supervision timeout: %d",
                connectionInterval, slaveLatency, supervisionTimeout));
    }

    @Override
    public double getValue(int sensorAttribute) {
        switch (sensorAttribute)
        {
            case ATTRIBUTE_CONNECTION_INTERVAL:
                return connectionInterval;
            case ATTRIBUTE_SLAVE_LATENCY:
                return slaveLatency;
            case ATTRIBUTE_SUPERVISION_TIMEOUT:
                return supervisionTimeout;
            default:
                return -1.0f;
        }
    }
}
