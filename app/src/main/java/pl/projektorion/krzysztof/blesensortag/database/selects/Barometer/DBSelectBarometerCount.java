package pl.projektorion.krzysztof.blesensortag.database.selects.Barometer;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectRecordsCountAbstract;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Barometer.DBTableBarometer;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectBarometerCount extends DBSelectRecordsCountAbstract {

    private DBSelectInterface barometerRecordsCount;

    public DBSelectBarometerCount(DBSelectInterface rootRecord) {
        super(rootRecord);
        this.barometerRecordsCount = new DBSelectBarometerCountData();
    }

    public DBSelectBarometerCount(Parcel in) {
        super(in);
        barometerRecordsCount = in.readParcelable(DBSelectInterface.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(barometerRecordsCount, flags);
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        barometerRecordsCount = new DBSelectBarometerCountData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return barometerRecordsCount;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> data = new ArrayList<>();
        data.add(barometerRecordsCount);
        return data;
    }

    @Override
    public String getLabel() {
        return DBSelectBarometerCountData.CSV_HEADER;
    }

    @Override
    protected String table_name() {
        return DBTableBarometer.TABLE_NAME;
    }

    @Override
    protected String grouping_column() {
        return DBTableBarometer.COLUMN_ROOT_REF;
    }

    public static final Parcelable.Creator<DBSelectBarometerCount> CREATOR = new Creator<DBSelectBarometerCount>() {
        @Override
        public DBSelectBarometerCount createFromParcel(Parcel source) {
            return new DBSelectBarometerCount(source);
        }

        @Override
        public DBSelectBarometerCount[] newArray(int size) {
            return new DBSelectBarometerCount[size];
        }
    };
}
