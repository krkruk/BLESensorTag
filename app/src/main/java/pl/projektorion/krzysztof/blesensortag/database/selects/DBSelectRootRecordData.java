package pl.projektorion.krzysztof.blesensortag.database.selects;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by krzysztof on 18.12.16.
 */

public class DBSelectRootRecordData extends DBSelectDataAbstract {
    public static final int ATTRIBUTE_DATE_SECONDS = 0x02;

    private long _id = -1;
    private long dateSeconds = -1;

    public DBSelectRootRecordData(int _id, long dateSeconds) {
        super();
        this._id = _id;
        this.dateSeconds = dateSeconds;
    }

    public DBSelectRootRecordData(Cursor record) {
        super(record);
        parse(record);
    }

    public DBSelectRootRecordData(Parcel in) {
        _id = in.readLong();
        dateSeconds = in.readLong();
    }

    /**
     * Get data associated with a Root table 'Record'.
     * @param recordAttribute Select an attribute associated with the class
     * @return long - _id of the record and long - dateSeconds of the record
     */
    @Override
    public Object getData(int recordAttribute) {
        switch (recordAttribute)
        {
            case ATTRIBUTE_DATE_SECONDS:
                return dateSeconds;
            case ATTRIBUTE_ID:
                return _id;
            default:
                return -1;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeLong(dateSeconds);
    }

    /**
     * Parse the cursor to receive data for
     * {@link pl.projektorion.krzysztof.blesensortag.database.tables.DBRootTableRecord}
     * @param record {@link Cursor} that contains data associated with a Roor record.
     */
    protected void parse(Cursor record)
    {
        _id = record.getInt(0);
        dateSeconds = record.getLong(1);
    }

    public static final Parcelable.Creator<DBSelectRootRecordData> CREATOR
            = new Parcelable.Creator<DBSelectRootRecordData>() {
        public DBSelectRootRecordData createFromParcel(Parcel in) {
            return new DBSelectRootRecordData(in);
        }

        public DBSelectRootRecordData[] newArray(int size) {
            return new DBSelectRootRecordData[size];
        }
    };
}
