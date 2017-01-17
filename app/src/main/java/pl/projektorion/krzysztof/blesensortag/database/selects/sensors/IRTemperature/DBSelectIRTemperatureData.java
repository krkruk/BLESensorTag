package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.IRTemperature;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * Created by krzysztof on 22.12.16.
 */

public class DBSelectIRTemperatureData implements DBSelectInterface {

    public static final int ATTRIBUTE_TIME = 0x10;
    public static final int ATTRIBUTE_OBJECT_TEMPERATURE = 0x11;
    public static final int ATTRIBUTE_AMBIENT_TEMPERATURE = 0x12;

    public static final String CSV_HEADER = "time,object,ambient";

    private double time = 0;
    private double temperatureObject = 0.0f;
    private double temperatureAmbient = 0.0f;

    public DBSelectIRTemperatureData(double time, Cursor cursor) {
        this.time = time;
        parse(cursor);
    }

    public DBSelectIRTemperatureData(Parcel in) {
        time = in.readDouble();
        temperatureObject = in.readDouble();
        temperatureAmbient = in.readDouble();
    }

    @Override
    public Object getData(int recordAttribute) {
        switch (recordAttribute)
        {
            case ATTRIBUTE_TIME:
                return time;
            case ATTRIBUTE_OBJECT_TEMPERATURE:
                return temperatureObject;
            case ATTRIBUTE_AMBIENT_TEMPERATURE:
                return temperatureAmbient;
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
        dest.writeDouble(temperatureObject);
        dest.writeDouble(temperatureAmbient);
    }

    @Override
    public String toString() {
        return time +
                "," + temperatureObject +
                "," + temperatureAmbient;
    }

    private void parse(Cursor cursor)
    {
        temperatureObject = cursor.getDouble(0);
        temperatureAmbient = cursor.getDouble(1);
    }

    public static final Parcelable.Creator<DBSelectIRTemperatureData> CREATOR = new Creator<DBSelectIRTemperatureData>() {
        @Override
        public DBSelectIRTemperatureData createFromParcel(Parcel source) {
            return new DBSelectIRTemperatureData(source);
        }

        @Override
        public DBSelectIRTemperatureData[] newArray(int size) {
            return new DBSelectIRTemperatureData[size];
        }
    };
}
