package pl.projektorion.krzysztof.blesensortag.database.selects.Humidity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * Created by krzysztof on 22.12.16.
 */

public class DBSelectHumidityData implements DBSelectInterface {

    public static final int ATTRIBUTE_TIME = 0x10;
    public static final int ATTRIBUTE_RELATIVE_HUMIDITY = 0x11;
    public static final int ATTRIBUTE_TEMPERATURE = 0x12;
    public static final int ATTRIBUTE_CSV_HEADER = 0x13;

    public static final String CSV_HEADER = "time,humidity,temperature";

    private long time = 0;
    private long relativeHumidity = 0;
    private long temperature = 0;

    public DBSelectHumidityData(long time, Cursor cursor) {
        this.time = time;
        parse(cursor);
    }

    public DBSelectHumidityData(Parcel in) {
        time = in.readLong();
        relativeHumidity = in.readLong();
        temperature = in.readLong();
    }

    @Override
    public Object getData(int recordAttribute) {
        switch (recordAttribute)
        {
            case ATTRIBUTE_TIME:
                return time;
            case ATTRIBUTE_RELATIVE_HUMIDITY:
                return relativeHumidity;
            case ATTRIBUTE_TEMPERATURE:
                return temperature;
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
        dest.writeLong(relativeHumidity);
        dest.writeLong(temperature);
    }

    @Override
    public String toString() {
        return time +
                "," + relativeHumidity +
                "," + temperature;
    }

    protected void parse(Cursor cursor)
    {
        relativeHumidity = cursor.getLong(0);
        temperature = cursor.getLong(1);
    }

    public static final Parcelable.Creator<DBSelectHumidityData> CREATOR = new Creator<DBSelectHumidityData>() {
        @Override
        public DBSelectHumidityData createFromParcel(Parcel source) {
            return new DBSelectHumidityData(source);
        }

        @Override
        public DBSelectHumidityData[] newArray(int size) {
            return new DBSelectHumidityData[size];
        }
    };
}
