package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * Created by krzysztof on 14.01.17.
 */

public class DBSelectStethoscopeData implements DBSelectInterface {
    public static final int ATTRIBUTE_FIRST = 0x01;

    public static final String CSV_HEADER = "time,first";

    private long time = 0;
    private double first = 0.0f;

    public DBSelectStethoscopeData(long time, Cursor parse) {
        this.time = time;
    }

    public DBSelectStethoscopeData(Parcel in) {
        this.time = in.readLong();
        this.first = in.readDouble();
    }

    @Override
    public Object getData(int recordAttribute) {
        switch (recordAttribute)
        {
            case ATTRIBUTE_FIRST:
                return first;
            case ATTRIBUTE_CSV_HEADER:
                return CSV_HEADER;
            default: return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeDouble(first);
    }

    @Override
    public String toString() {
        return  time +
                "," + first;
    }

    protected void parse(Cursor cursor)
    {
        first = cursor.getDouble(0);
    }

    public static final Parcelable.Creator<DBSelectStethoscopeData> CREATOR = new Creator<DBSelectStethoscopeData>() {
        @Override
        public DBSelectStethoscopeData createFromParcel(Parcel source) {
            return new DBSelectStethoscopeData(source);
        }

        @Override
        public DBSelectStethoscopeData[] newArray(int size) {
            return new DBSelectStethoscopeData[size];
        }
    };
}
