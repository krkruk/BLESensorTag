package pl.projektorion.krzysztof.blesensortag.database.selects.Barometer;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectBarometerCountData implements DBSelectInterface {

    public static final int ATTRIBUTE_COUNT = 0x30;
    public static final int ATTRIBUTE_CSV_HEADER = 0x31;

    public static final String CSV_HEADER = "COUNT";
    private long count = 0;

    public DBSelectBarometerCountData() {
    }

    public DBSelectBarometerCountData(Cursor cursor) {
        count = cursor.getLong(0);
    }

    public DBSelectBarometerCountData(Parcel in)
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

    public static final Parcelable.Creator<DBSelectBarometerCountData> CREATOR = new Creator<DBSelectBarometerCountData>() {
        @Override
        public DBSelectBarometerCountData createFromParcel(Parcel source) {
            return new DBSelectBarometerCountData(source);
        }

        @Override
        public DBSelectBarometerCountData[] newArray(int size) {
            return new DBSelectBarometerCountData[size];
        }
    };
}
