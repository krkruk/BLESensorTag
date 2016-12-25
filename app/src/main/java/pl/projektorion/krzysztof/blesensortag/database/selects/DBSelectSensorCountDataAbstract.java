package pl.projektorion.krzysztof.blesensortag.database.selects;

import android.database.Cursor;
import android.os.Parcel;

/**
 * Created by krzysztof on 25.12.16.
 */

public abstract class DBSelectSensorCountDataAbstract implements DBSelectInterface {
    public static final int ATTRIBUTE_COUNT = 0x30;
    public static final int ATTRIBUTE_CSV_HEADER = 0x31;

    public static final String CSV_HEADER = "COUNT";
    private long count = 0;

    public DBSelectSensorCountDataAbstract() {
    }

    public DBSelectSensorCountDataAbstract(Cursor cursor) {
        count = cursor.getLong(0);
    }

    public DBSelectSensorCountDataAbstract(Parcel in)
    {
        count = in.readLong();
    }

    @Override
    public Object getData(int recordAttribute) {
        switch (recordAttribute)
        {
            case ATTRIBUTE_COUNT:
                return count;
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
        dest.writeLong(count);
    }

    @Override
    public String toString() {
        return Long.toString(count);
    }
}
