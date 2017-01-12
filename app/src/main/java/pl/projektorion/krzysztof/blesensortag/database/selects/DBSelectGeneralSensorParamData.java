package pl.projektorion.krzysztof.blesensortag.database.selects;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectDataAbstract;


/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectGeneralSensorParamData extends DBSelectDataAbstract {
    public static final int ATTRIBUTE_RECORD_ID = 0x02;
    public static final int ATTRIBUTE_NOTIFY_PERIOD = 0x03;

    private static final int COLUMN_COUNT = 3;
    private long _id = -1;
    private long recordId = -1;
    private long notifyPeriod = -1;

    public DBSelectGeneralSensorParamData() {
        super();
    }

    public DBSelectGeneralSensorParamData(Cursor cursor) {
        parse(cursor);
    }

    public DBSelectGeneralSensorParamData(Parcel in)
    {
        _id = in.readLong();
        recordId = in.readLong();
        notifyPeriod = in.readLong();
    }

    @Override
    protected void parse(Cursor cursor) {
        if( cursor.getColumnCount() != COLUMN_COUNT) {
            Log.d("BarParam", "Column count invalid");
            return;
        }
        _id = cursor.getLong(0);
        recordId = cursor.getLong(1);
        notifyPeriod = cursor.getLong(2);

        Log.i("Parse", String.format("_id: %d, recId: %d, notifyPer: %d", _id, recordId, notifyPeriod));
    }

    @Override
    public Object getData(int recordAttribute) {
        switch (recordAttribute)
        {
            case ATTRIBUTE_ID:
                return _id;
            case ATTRIBUTE_RECORD_ID:
                return recordId;
            case ATTRIBUTE_NOTIFY_PERIOD:
                return notifyPeriod;
            default: return 0;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeLong(recordId);
        dest.writeLong(notifyPeriod);
    }

    public static final Parcelable.Creator<DBSelectGeneralSensorParamData> CREATOR = new Creator<DBSelectGeneralSensorParamData>() {
        @Override
        public DBSelectGeneralSensorParamData createFromParcel(Parcel source) {
            return new DBSelectGeneralSensorParamData(source);
        }

        @Override
        public DBSelectGeneralSensorParamData[] newArray(int size) {
            return new DBSelectGeneralSensorParamData[size];
        }
    };

    @Override
    public String toString() {
        return "DBSelectGeneralSensorParamData{" +
                "_id=" + _id +
                ", recordId=" + recordId +
                ", notifyPeriod=" + notifyPeriod +
                '}';
    }
}
