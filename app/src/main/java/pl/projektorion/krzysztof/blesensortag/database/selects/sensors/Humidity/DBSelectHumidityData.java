package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Humidity;

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

    public static final String CSV_HEADER = "time,humidity,temperature";

    private double time = 0;
    private double relativeHumidity = 0.0f;
    private double temperature = 0.0f;

    public DBSelectHumidityData(double time, Cursor cursor) {
        this.time = time;
        parse(cursor);
    }

    public DBSelectHumidityData(Parcel in) {
        time = in.readDouble();
        relativeHumidity = in.readDouble();
        temperature = in.readDouble();
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
        dest.writeDouble(time);
        dest.writeDouble(relativeHumidity);
        dest.writeDouble(temperature);
    }

    @Override
    public String toString() {
        return time +
                "," + relativeHumidity +
                "," + temperature;
    }

    protected void parse(Cursor cursor)
    {
        relativeHumidity = cursor.getDouble(0);
        temperature = cursor.getDouble(1);
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
