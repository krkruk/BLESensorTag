package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.IRTemperature;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorCountAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature.DBTableIRTemperature;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectIRTemperatureCount extends DBSelectSensorCountAbstract {
    private DBSelectInterface irTemperatureRecordsCount;

    public DBSelectIRTemperatureCount(DBSelectInterface rootRecord) {
        super(rootRecord);
        this.irTemperatureRecordsCount = new DBSelectIRTemperatureCountData();
    }

    public DBSelectIRTemperatureCount(Parcel in) {
        super(in);
        this.irTemperatureRecordsCount = in.readParcelable(DBSelectInterface.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(irTemperatureRecordsCount, flags);
    }

    @Override
    public String getLabel() {
        return DBSelectIRTemperatureCountData.CSV_HEADER;
    }

    @Override
    protected String table_name() {
        return DBTableIRTemperature.TABLE_NAME;
    }

    @Override
    protected String grouping_column() {
        return DBTableIRTemperature.COLUMN_ROOT_REF;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        irTemperatureRecordsCount = new DBSelectIRTemperatureCountData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return irTemperatureRecordsCount;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> data = new ArrayList<>();
        data.add(irTemperatureRecordsCount);
        return data;
    }

    public static final Parcelable.Creator<DBSelectIRTemperatureCount> CREATOR = new Creator<DBSelectIRTemperatureCount>() {
        @Override
        public DBSelectIRTemperatureCount createFromParcel(Parcel source) {
            return new DBSelectIRTemperatureCount(source);
        }

        @Override
        public DBSelectIRTemperatureCount[] newArray(int size) {
            return new DBSelectIRTemperatureCount[size];
        }
    };
}
