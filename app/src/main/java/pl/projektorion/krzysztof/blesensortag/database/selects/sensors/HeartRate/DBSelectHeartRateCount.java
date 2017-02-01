package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.HeartRate;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorCountAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.HeartRate.DBTableHeartRateParam;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBSelectHeartRateCount extends DBSelectSensorCountAbstract {

    private DBSelectHeartRateCountData heartRateCountData;

    public DBSelectHeartRateCount(DBSelectInterface rootRecord) {
        super(rootRecord);
        heartRateCountData = new DBSelectHeartRateCountData();
    }

    public DBSelectHeartRateCount(Parcel in) {
        super(in);
        heartRateCountData = in.readParcelable(DBSelectHeartRateCountData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(heartRateCountData, flags);
    }

    @Override
    protected String table_name() {
        return DBTableHeartRateParam.TABLE_NAME;
    }

    @Override
    protected String grouping_column() {
        return DBTableHeartRateParam.COLUMN_ROOT_REF;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        heartRateCountData = new DBSelectHeartRateCountData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return heartRateCountData;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> data = new ArrayList<>();
        data.add(heartRateCountData);
        return data;
    }

    @Override
    public String getLabel() {
        return DBSelectHeartRateCountData.CSV_HEADER;
    }

    public static final Parcelable.Creator<DBSelectHeartRateCount> CREATOR = new Creator<DBSelectHeartRateCount>() {
        @Override
        public DBSelectHeartRateCount createFromParcel(Parcel source) {
            return new DBSelectHeartRateCount(source);
        }

        @Override
        public DBSelectHeartRateCount[] newArray(int size) {
            return new DBSelectHeartRateCount[size];
        }
    };
}
