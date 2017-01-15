package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectRootRecordData;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorCountAbstract;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.OpticalSensor.DBSelectOpticalSensorParam;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope.DBTableStethoscope;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope.DBTableStethoscopeParam;

/**
 * Created by krzysztof on 15.01.17.
 */

public class DBSelectStethoscopeCount extends DBSelectSensorCountAbstract {
    private DBSelectInterface stethoscopeDataCount;

    public DBSelectStethoscopeCount(DBSelectInterface rootRecord) {
        super(rootRecord);
    }

    public DBSelectStethoscopeCount(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    protected String table_name() {
        return DBTableStethoscope.TABLE_NAME;
    }

    @Override
    protected String grouping_column() {
        return DBTableStethoscope.COLUMN_ROOT_REF;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        stethoscopeDataCount = new DBSelectStethoscopeCountData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return stethoscopeDataCount;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> data = new ArrayList<>();
        data.add(stethoscopeDataCount);
        return data;
    }

    public static final Parcelable.Creator<DBSelectStethoscopeCount> CREATOR = new Creator<DBSelectStethoscopeCount>() {
        @Override
        public DBSelectStethoscopeCount createFromParcel(Parcel source) {
            return new DBSelectStethoscopeCount(source);
        }

        @Override
        public DBSelectStethoscopeCount[] newArray(int size) {
            return new DBSelectStethoscopeCount[size];
        }
    };
}
