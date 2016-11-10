package pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.ConnectionControl;

import pl.projektorion.krzysztof.blesensortag.utils.ByteOperation;

/**
 * Created by krzysztof on 10.11.16.
 */

public class ConnectionControlRequest implements ConnectionControlRequestInterface {
    private int maxConnInterval;
    private int minConnInterval;
    private int slaveLatency;
    private int supervisionTimeout;

    private static final int INT_16_MASK = 0xffff;

    public ConnectionControlRequest() {
        this.maxConnInterval = 10;
        this.minConnInterval = 5;
        this.slaveLatency = 0;
        this.supervisionTimeout = 10000;
    }

    @Override
    public void requestConnectionInterval(int min, int max) {
        minConnInterval = min & INT_16_MASK;
        maxConnInterval = max & INT_16_MASK;
    }

    @Override
    public void requestSlaveLatency(int latency) {
        slaveLatency = latency & INT_16_MASK;
    }

    @Override
    public void requestSupervisionTimeout(int timeout) {
        supervisionTimeout = timeout & INT_16_MASK;
    }

    @Override
    public byte[] getRequest() {
        final int REQUEST_SIZE = 8;

        final byte[][] requestToFlatten = {
                parse_max_conn_interval(),
                parse_min_conn_interval(),
                parse_slave_latency(),
                parse_supervision_timeout()
        };

        return flatten_array(requestToFlatten, REQUEST_SIZE);
    }

    private byte[] parse_max_conn_interval()
    {
        return parse(maxConnInterval);
    }

    private byte[] parse_min_conn_interval()
    {
        return parse(minConnInterval);
    }

    private byte[] parse_slave_latency()
    {
        return parse(slaveLatency);
    }

    private byte[] parse_supervision_timeout()
    {
        return parse(supervisionTimeout);
    }

    private byte[] parse(int valueToUnsignedLittleEndian)
    {
        byte[] bigEndian = ByteOperation.intToBytes(valueToUnsignedLittleEndian);
        return new byte[] {bigEndian[3], bigEndian[2]};
    }

    private byte[] flatten_array(byte[][] array, int length)
    {
        final byte[] request = new byte[length];
        int index = 0;
        for(byte[] dataArray : array)
            for(byte data : dataArray)
                request[index++] = data;
        return request;
    }
}
