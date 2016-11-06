package pl.projektorion.krzysztof.blesensortag.utils;

import java.nio.ByteBuffer;

/**
 * Created by krzysztof on 06.11.16.
 */

public class ByteOperation {
    public static int bytesToInt(byte[] data)
    {
        if( data.length != 4 ) return -1;
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        return byteBuffer.getInt();
    }

    public static byte[] intToBytes(int data)
    {
        final int INT_SIZE = 4;
        byte[] buffer = new byte[INT_SIZE];
        for(int i = 0; i < INT_SIZE; i++)
            buffer[i] = (byte) (data >>((INT_SIZE-1-i)*8) );
        return buffer;
    }

    public static byte[] littleToBigEndian(byte[] littleEndian)
    {
        byte[] bigEndian = new byte[littleEndian.length];
        int ii = 0;
        for(int i = littleEndian.length - 1; i >= 0; i--)
            bigEndian[ii++] = littleEndian[i];

        return bigEndian;
    }
}
