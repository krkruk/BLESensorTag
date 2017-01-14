package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope.DBTableStethoscope;

/**
 * Created by krzysztof on 14.01.17.
 */

public class DBSelectStethoscope extends DBSelectSensorAbstract {
    List<DBSelectStethoscopeData> stethoscopeDatas;

    public DBSelectStethoscope(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        super(rootRecord, sensorRecord);
        stethoscopeDatas = new ArrayList<>();
    }

    public DBSelectStethoscope(Parcel in) {
        super(in);
        stethoscopeDatas = new ArrayList<>();
        in.readTypedList(stethoscopeDatas, DBSelectStethoscopeData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(stethoscopeDatas);
    }

    @Override
    protected void parse_cursor_data(long time, Cursor cursor) {
        stethoscopeDatas.add(new DBSelectStethoscopeData(time, cursor));
    }

    @Override
    protected String select_db_columns() {
        return DBTableStethoscope.COLUMN_DATA;
    }

    @Override
    protected String select_table_name() {
        return DBTableStethoscope.TABLE_NAME;
    }

    @Override
    protected String select_extract_records_where() {
        return DBTableStethoscope.COLUMN_ROOT_REF;
    }

    @Override
    public DBSelectInterface getRecord() {
        final int dataSize = stethoscopeDatas.size();
        return dataSize > 0
                ? stethoscopeDatas.get(dataSize-1)
                : null;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        return stethoscopeDatas;
    }

    @Override
    public String getLabel() {
        return DBSelectStethoscopeData.CSV_HEADER;
    }

    public static final Parcelable.Creator<DBSelectStethoscope> CREATOR = new Creator<DBSelectStethoscope>() {
        @Override
        public DBSelectStethoscope createFromParcel(Parcel source) {
            return new DBSelectStethoscope(source);
        }

        @Override
        public DBSelectStethoscope[] newArray(int size) {
            return new DBSelectStethoscope[size];
        }
    };
}
