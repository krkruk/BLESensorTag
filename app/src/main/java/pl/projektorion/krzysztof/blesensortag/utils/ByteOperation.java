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

    public static byte[] littleToBigEndian(byte[] littleEndian)
    {
        byte[] bigEndian = new byte[littleEndian.length];
        int ii = 0;
        for(int i = littleEndian.length - 1; i >= 0; i--)
            bigEndian[ii++] = littleEndian[i];

        return bigEndian;
    }
}
