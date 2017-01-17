package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * Created by krzysztof on 15.01.17.
 */

public class DBSelectStethoscopeParamRootData implements DBSelectInterface {
    public static final int ATTRIBUTE_ID_RECORD = 0x05;
    public static final int ATTRIBUTE_NOTIFY_PERIOD = 0x06;
    public static final int ATTRIBUTE_DATE_SECONDS = 0x07;

    public static final String CSV_HEADER = "_id,ID_RECORD,notifyPeriod,dateSeconds";

    private long _id = 0;
    private long idRecord = 0;
    private double notifyPeriod = 0;
    private long dateSeconds = 0;

    public DBSelectStethoscopeParamRootData() {
    }

    public DBSelectStethoscopeParamRootData(Cursor cursor) {
        parse(cursor);
    }

    public DBSelectStethoscopeParamRootData(Parcel in) {
        _id = in.readLong();
        idRecord = in.readLong();
        notifyPeriod = in.readDouble();
        dateSeconds = in.readLong();
    }

    @Override
    public Object getData(int recordAttribute) {
        switch (recordAttribute)
        {
            case ATTRIBUTE_ID:
                return _id;
            case ATTRIBUTE_ID_RECORD:
                return idRecord;
            case ATTRIBUTE_NOTIFY_PERIOD:
                return notifyPeriod;
            case ATTRIBUTE_DATE_SECONDS:
                return dateSeconds;
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
        dest.writeLong(_id);
        dest.writeLong(idRecord);
        dest.writeDouble(notifyPeriod);
        dest.writeLong(dateSeconds);
    }

    @Override
    public String toString() {
        return _id +
                ", " + idRecord +
                "," + notifyPeriod +
                "," + dateSeconds;
    }

    private void parse(Cursor cursor)
    {
        _id = cursor.getLong(0);
        idRecord = cursor.getLong(1);
        notifyPeriod = cursor.getDouble(2);
        dateSeconds = cursor.getLong(3);
    }

    public static final Parcelable.Creator<DBSelectStethoscopeParamRootData> CREATOR = new Creator<DBSelectStethoscopeParamRootData>() {
        @Override
        public DBSelectStethoscopeParamRootData createFromParcel(Parcel source) {
            return new DBSelectStethoscopeParamRootData(source);
        }

        @Override
        public DBSelectStethoscopeParamRootData[] newArray(int size) {
            return new DBSelectStethoscopeParamRootData[size];
        }
    };
}
