package pl.projektorion.krzysztof.blesensortag.database.selects.Movement;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * Created by krzysztof on 22.12.16.
 */

public class DBSelectMovementData implements DBSelectInterface {

    public static final int ATTRIBUTE_ACC_Z = 0x20;
    public static final int ATTRIBUTE_ACC_Y = 0x21;
    public static final int ATTRIBUTE_ACC_X = 0x22;
    public static final int ATTRIBUTE_GYRO_Z = 0x23;
    public static final int ATTRIBUTE_GYRO_Y = 0x24;
    public static final int ATTRIBUTE_GYRO_X = 0x25;
    public static final int ATTRIBUTE_MAGNET_Z = 0x26;
    public static final int ATTRIBUTE_MAGNET_Y = 0x27;
    public static final int ATTRIBUTE_MAGNET_X = 0x28;

    public static final int ATTRIBUTE_MEASUREMENT = 0x10;
    public static final int ATTRIBUTE_CSV_HEADER = 0x13;

    public static final String CSV_HEADER =
            "measurement,Acc_X,Acc_Y,Acc_Z," +
                    "Gyr_X,Gyr_Y,Gyr_Z," +
                    "Mag_X,Mag_Y,Mag_Z";

    private long measurement = 0;
    private float accX = 0;
    private float accY = 0;
    private float accZ = 0;

    private float gyroX = 0;
    private float gyroY = 0;
    private float gyroZ = 0;

    private float magnetX = 0;
    private float magnetY = 0;
    private float magnetZ = 0;

    public DBSelectMovementData(long measurement, Cursor cursor) {
        this.measurement = measurement;
        parse(cursor);
    }

    public DBSelectMovementData(Parcel in)
    {
        measurement = in.readLong();
        accX = in.readFloat();
        accY = in.readFloat();
        accZ = in.readFloat();

        gyroX = in.readFloat();
        gyroY = in.readFloat();
        gyroZ = in.readFloat();

        magnetX = in.readFloat();
        magnetY = in.readFloat();
        magnetZ = in.readFloat();
    }

    @Override
    public Object getData(int recordAttribute) {
        switch (recordAttribute)
        {
            case ATTRIBUTE_ACC_Z:
                return accZ;
            case ATTRIBUTE_ACC_Y:
                return accY;
            case ATTRIBUTE_ACC_X:
                return accX;

            case ATTRIBUTE_GYRO_Z:
                return gyroZ;
            case ATTRIBUTE_GYRO_Y:
                return gyroY;
            case ATTRIBUTE_GYRO_X:
                return gyroX;

            case ATTRIBUTE_MAGNET_Z:
                return magnetZ;
            case ATTRIBUTE_MAGNET_Y:
                return magnetY;
            case ATTRIBUTE_MAGNET_X:
                return magnetX;

            case ATTRIBUTE_MEASUREMENT:
                return measurement;
            case ATTRIBUTE_CSV_HEADER:
                return CSV_HEADER;
            default:
                return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(measurement);
        dest.writeFloat(accX);
        dest.writeFloat(accY);
        dest.writeFloat(accZ);
        dest.writeFloat(gyroX);
        dest.writeFloat(gyroY);
        dest.writeFloat(gyroZ);
        dest.writeFloat(magnetX);
        dest.writeFloat(magnetY);
        dest.writeFloat(magnetZ);
    }

    @Override
    public String toString() {
        return measurement +
                "," + accX +
                "," + accY +
                "," + accZ +
                "," + gyroX +
                "," + gyroY +
                "," + gyroZ +
                "," + magnetX +
                "," + magnetY +
                "," + magnetZ;
    }

    private void parse(Cursor cursor)
    {
        accX = cursor.getFloat(0);
        accY = cursor.getFloat(1);
        accZ = cursor.getFloat(2);

        gyroX = cursor.getFloat(3);
        gyroY = cursor.getFloat(4);
        gyroZ = cursor.getFloat(5);

        magnetX = cursor.getFloat(6);
        magnetY = cursor.getFloat(7);
        magnetZ = cursor.getFloat(8);
    }

    public static final Parcelable.Creator<DBSelectMovementData> CREATOR = new Creator<DBSelectMovementData>() {
        @Override
        public DBSelectMovementData createFromParcel(Parcel source) {
            return new DBSelectMovementData(source);
        }

        @Override
        public DBSelectMovementData[] newArray(int size) {
            return new DBSelectMovementData[size];
        }
    };
}
