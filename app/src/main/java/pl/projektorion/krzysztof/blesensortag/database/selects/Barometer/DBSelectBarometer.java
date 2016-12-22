package pl.projektorion.krzysztof.blesensortag.database.selects.Barometer;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Barometer.DBTableBarometer;

/**
 * Created by krzysztof on 22.12.16.
 */

public class DBSelectBarometer extends DBSelectSensorAbstract {
    private List<DBSelectBarometerData> barometerRecords;

    public DBSelectBarometer(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        super(rootRecord, sensorRecord);
        barometerRecords = new ArrayList<>();
    }

    public DBSelectBarometer(Parcel in) {
        super(in);
        barometerRecords = new ArrayList<>();
        in.readTypedList(barometerRecords, DBSelectBarometerData.CREATOR);
    }

    @Override
    protected String select_db_columns() {
        return DBTableBarometer.COLUMN_PRESSURE + ", "
                + DBTableBarometer.COLUMN_TEMPERATURE;
    }

    @Override
    protected String select_table_name() {
        return DBTableBarometer.TABLE_NAME;
    }

    @Override
    protected String select_extract_records_where() {
        return DBTableBarometer.COLUMN_ROOT_REF;
    }

    @Override
    protected void parse_cursor_data(long time, Cursor cursor) {
        barometerRecords.add(new DBSelectBarometerData(time, cursor));
    }

    @Override
    public DBSelectInterface getRecord() {
        final int barometerDataSize = barometerRecords.size();
        return barometerDataSize > 0
                ? barometerRecords.get(barometerDataSize - 1)
                : null;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        return barometerRecords;
    }

    @Override
    public String getLabel() {
        return DBSelectBarometerData.CSV_HEADER;
    }


    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(barometerRecords);
    }

    public static final Parcelable.Creator<DBSelectBarometer> CREATOR = new Creator<DBSelectBarometer>() {
        @Override
        public DBSelectBarometer createFromParcel(Parcel source) {
            return new DBSelectBarometer(source);
        }

        @Override
        public DBSelectBarometer[] newArray(int size) {
            return new DBSelectBarometer[size];
        }
    };
}
