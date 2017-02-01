package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.HeartRate;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBSelectHeartRateData implements DBSelectInterface {

    public static final int ATTRIBUTE_TIME = 0x10;
    public static final int ATTRIBUTE_HEART_RATE = 0x11;

    public static final String CSV_HEADER = "time,heart_rate";

    private double time = 0;
    private double heartRate = 0.0f;

    public DBSelectHeartRateData(double time, Cursor cursor) {
        this.time = time;
        parse(cursor);
    }

    public DBSelectHeartRateData(Parcel in) {
        time = in.readDouble();
        heartRate = in.readDouble();
    }

    @Override
    public Object getData(int recordAttribute) {
        switch (recordAttribute)
        {
            case ATTRIBUTE_TIME:
                return time;

            case ATTRIBUTE_HEART_RATE:
                return heartRate;

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
        dest.writeDouble(time);
        dest.writeDouble(heartRate);
    }

    @Override
    public String toString() {
        return time + "," + heartRate;
    }

    private void parse(Cursor cursor)
    {
        heartRate = cursor.getDouble(0);
    }

    public static final Parcelable.Creator<DBSelectHeartRateData> CREATOR = new Creator<DBSelectHeartRateData>() {
        @Override
        public DBSelectHeartRateData createFromParcel(Parcel source) {
            return new DBSelectHeartRateData(source);
        }

        @Override
        public DBSelectHeartRateData[] newArray(int size) {
            return new DBSelectHeartRateData[size];
        }
    };
}
