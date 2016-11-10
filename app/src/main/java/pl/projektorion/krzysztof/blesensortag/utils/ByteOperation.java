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

    public static short bytesToShort(byte[] data)
    {
        if( data.length != 2 ) return -1;
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        return byteBuffer.getShort();
    }

    public static byte[] littleToBigEndian(byte[] littleEndian)
    {
        byte[] bigEndian = new byte[littleEndian.length];
        int ii = 0;
        for(int i = littleEndian.length - 1; i >= 0; i--)
            bigEndian[ii++] = littleEndian[i];

        return bigEndian;
    }

    public static byte[] bigToLittleEndian(byte[] bigEndian)
    {
        final int BIG_ENDIAN_LENGTH = bigEndian.length;
        byte[] littleEndian = new byte[BIG_ENDIAN_LENGTH];
        int ii = 0;
        for(int i = BIG_ENDIAN_LENGTH - 1; i >= 0; i--)
            littleEndian[ii++] = bigEndian[i];

        return littleEndian;
    }
}
