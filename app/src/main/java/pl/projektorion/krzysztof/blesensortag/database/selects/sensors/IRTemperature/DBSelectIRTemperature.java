package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.IRTemperature;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature.DBTableIRTemperature;

/**
 * Created by krzysztof on 22.12.16.
 */

public class DBSelectIRTemperature extends DBSelectSensorAbstract {
    private List<DBSelectIRTemperatureData> irTemperatureRecords;

    public DBSelectIRTemperature(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        super(rootRecord, sensorRecord);
        irTemperatureRecords = new ArrayList<>();
    }

    public DBSelectIRTemperature(Parcel in) {
        super(in);
        irTemperatureRecords = new ArrayList<>();
        in.readTypedList(irTemperatureRecords, DBSelectIRTemperatureData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(irTemperatureRecords);
    }

    @Override
    protected void parse_cursor_data(long time, Cursor cursor) {
        irTemperatureRecords.add(new DBSelectIRTemperatureData(time, cursor));
    }

    @Override
    protected String select_db_columns() {
        return DBTableIRTemperature.COLUMN_OBJ_TEMP + ", "
                + DBTableIRTemperature.COLUMN_AMBIENT_TEMP;
    }

    @Override
    protected String select_table_name() {
        return DBTableIRTemperature.TABLE_NAME;
    }

    @Override
    protected String select_extract_records_where() {
        return DBTableIRTemperature.COLUMN_ROOT_REF;
    }

    @Override
    public DBSelectInterface getRecord() {
        final int dataSize = irTemperatureRecords.size();
        return dataSize > 0
                ? irTemperatureRecords.get(dataSize-1)
                : null;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        return irTemperatureRecords;
    }

    @Override
    public String getLabel() {
        return DBSelectIRTemperatureData.CSV_HEADER;
    }

    public static final Parcelable.Creator<DBSelectIRTemperature> CREATOR = new Creator<DBSelectIRTemperature>() {
        @Override
        public DBSelectIRTemperature createFromParcel(Parcel source) {
            return new DBSelectIRTemperature(source);
        }

        @Override
        public DBSelectIRTemperature[] newArray(int size) {
            return new DBSelectIRTemperature[size];
        }
    };
}
