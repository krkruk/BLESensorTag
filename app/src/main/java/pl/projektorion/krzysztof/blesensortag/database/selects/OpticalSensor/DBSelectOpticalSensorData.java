package pl.projektorion.krzysztof.blesensortag.database.selects.OpticalSensor;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * Created by krzysztof on 22.12.16.
 */

public class DBSelectOpticalSensorData implements DBSelectInterface {

    public static final int ATTRIBUTE_TIME = 0x10;
    public static final int ATTRIBUTE_LIGHT_INTENSITY = 0x11;

    public static final String CSV_HEADER = "time,intensity";


    private long time = 0;
    private double lightIntensity = 0.0f;

    public DBSelectOpticalSensorData(long time, Cursor cursor) {
        this.time = time;
        this.lightIntensity = cursor.getDouble(0);
    }

    public DBSelectOpticalSensorData(Parcel in)
    {
        time = in.readLong();
        lightIntensity = in.readDouble();
    }

    @Override
    public Object getData(int recordAttribute) {
        switch (recordAttribute)
        {
            case ATTRIBUTE_TIME:
                return time;
            case ATTRIBUTE_LIGHT_INTENSITY:
                return lightIntensity;
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
        dest.writeLong(time);
        dest.writeDouble(lightIntensity);
    }

    @Override
    public String toString() {
        return time +
                "," + lightIntensity;
    }

    public static final Parcelable.Creator<DBSelectOpticalSensorData> CREATOR = new Creator<DBSelectOpticalSensorData>() {
        @Override
        public DBSelectOpticalSensorData createFromParcel(Parcel source) {
            return new DBSelectOpticalSensorData(source);
        }

        @Override
        public DBSelectOpticalSensorData[] newArray(int size) {
            return new DBSelectOpticalSensorData[size];
        }
    };
}
