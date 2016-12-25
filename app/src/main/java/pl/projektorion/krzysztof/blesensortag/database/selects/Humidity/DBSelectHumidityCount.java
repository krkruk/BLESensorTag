package pl.projektorion.krzysztof.blesensortag.database.selects.Humidity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorCountAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity.DBTableHumidity;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectHumidityCount extends DBSelectSensorCountAbstract {

    private DBSelectInterface humidityRecordsCount;

    public DBSelectHumidityCount(DBSelectInterface rootRecord) {
        super(rootRecord);
        this.humidityRecordsCount = new DBSelectHumidityCountData();
    }

    public DBSelectHumidityCount(Parcel in) {
        super(in);
        this.humidityRecordsCount = in.readParcelable(DBSelectInterface.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(humidityRecordsCount, flags);
    }

    @Override
    protected String table_name() {
        return DBTableHumidity.TABLE_NAME;
    }

    @Override
    protected String grouping_column() {
        return DBTableHumidity.COLUMN_ROOT_REF;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        humidityRecordsCount = new DBSelectHumidityCountData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return humidityRecordsCount;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> data = new ArrayList<>();
        data.add(humidityRecordsCount);
        return data;
    }

    @Override
    public String getLabel() {
        return DBSelectHumidityCountData.CSV_HEADER;
    }

    public static final Parcelable.Creator<DBSelectHumidityCount> CREATOR = new Creator<DBSelectHumidityCount>() {
        @Override
        public DBSelectHumidityCount createFromParcel(Parcel source) {
            return new DBSelectHumidityCount(source);
        }

        @Override
        public DBSelectHumidityCount[] newArray(int size) {
            return new DBSelectHumidityCount[size];
        }
    };
}
