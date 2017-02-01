package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.HeartRate;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.HeartRate.DBTableHeartRate;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBSelectHeartRate extends DBSelectSensorAbstract {

    private List<DBSelectHeartRateData> heartRateDatas;

    public DBSelectHeartRate(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        super(rootRecord, sensorRecord);
        this.heartRateDatas = new ArrayList<>();
    }

    public DBSelectHeartRate(Parcel in) {
        super(in);
        this.heartRateDatas = new ArrayList<>();
        in.readTypedList(heartRateDatas, DBSelectHeartRateData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(heartRateDatas);
    }

    @Override
    protected void parse_cursor_data(double time, Cursor cursor) {
        heartRateDatas.add(new DBSelectHeartRateData(time, cursor));
    }

    @Override
    protected String select_db_columns() {
        return DBTableHeartRate.COLUMN_HEART_RATE;
    }

    @Override
    protected String select_table_name() {
        return DBTableHeartRate.TABLE_NAME;
    }

    @Override
    protected String select_extract_records_where() {
        return DBTableHeartRate.COLUMN_ROOT_REF;
    }

    @Override
    public DBSelectInterface getRecord() {
        final int heartRateDataSize = heartRateDatas.size();
        return heartRateDataSize > 0
                ? heartRateDatas.get(heartRateDataSize - 1)
                : null;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        return heartRateDatas;
    }

    @Override
    public String getLabel() {
        return DBSelectHeartRateData.CSV_HEADER;
    }

    public static final Parcelable.Creator<DBSelectHeartRate> CREATOR = new Creator<DBSelectHeartRate>() {
        @Override
        public DBSelectHeartRate createFromParcel(Parcel source) {
            return new DBSelectHeartRate(source);
        }

        @Override
        public DBSelectHeartRate[] newArray(int size) {
            return new DBSelectHeartRate[size];
        }
    };
}
