package pl.projektorion.krzysztof.blesensortag.database.selects.OpticalSensor;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorCountAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor.DBTableOpticalSensor;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectOpticalSensorCount extends DBSelectSensorCountAbstract {
    private DBSelectInterface opticalSensorRecordsCounter;

    public DBSelectOpticalSensorCount(DBSelectInterface rootRecord) {
        super(rootRecord);
        this.opticalSensorRecordsCounter = new DBSelectOpticalSensorCountData();
    }

    public DBSelectOpticalSensorCount(Parcel in) {
        super(in);
        this.opticalSensorRecordsCounter = in.readParcelable(DBSelectInterface.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(opticalSensorRecordsCounter, flags);
    }

    @Override
    public String getLabel() {
        return DBSelectOpticalSensorCountData.CSV_HEADER;
    }

    @Override
    protected String table_name() {
        return DBTableOpticalSensor.TABLE_NAME;
    }

    @Override
    protected String grouping_column() {
        return DBTableOpticalSensor.COLUMN_ROOT_REF;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        opticalSensorRecordsCounter = new DBSelectOpticalSensorCountData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return opticalSensorRecordsCounter;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> data = new ArrayList<>();
        data.add(opticalSensorRecordsCounter);
        return data;
    }

    public static final Parcelable.Creator<DBSelectOpticalSensorCount> CREATOR = new Creator<DBSelectOpticalSensorCount>() {
        @Override
        public DBSelectOpticalSensorCount createFromParcel(Parcel source) {
            return new DBSelectOpticalSensorCount(source);
        }

        @Override
        public DBSelectOpticalSensorCount[] newArray(int size) {
            return new DBSelectOpticalSensorCount[size];
        }
    };
}
