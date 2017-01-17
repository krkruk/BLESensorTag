package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Humidity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity.DBTableHumidity;

/**
 * Created by krzysztof on 22.12.16.
 */

public class DBSelectHumidity extends DBSelectSensorAbstract {
    private List<DBSelectHumidityData> humidityRecords;

    public DBSelectHumidity(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        super(rootRecord, sensorRecord);
        this.humidityRecords = new ArrayList<>();
    }

    public DBSelectHumidity(Parcel in) {
        super(in);
        this.humidityRecords = new ArrayList<>();
        in.readTypedList(humidityRecords, DBSelectHumidityData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(humidityRecords);
    }

    @Override
    protected void parse_cursor_data(double time, Cursor cursor) {
        humidityRecords.add(new DBSelectHumidityData(time, cursor));
    }

    @Override
    protected String select_db_columns() {
        return DBTableHumidity.COLUMN_REL_HUMIDITY + ", "
                + DBTableHumidity.COLUMN_TEMPERATURE;
    }

    @Override
    protected String select_table_name() {
        return DBTableHumidity.TABLE_NAME;
    }

    @Override
    protected String select_extract_records_where() {
        return DBTableHumidity.COLUMN_ROOT_REF;
    }

    @Override
    public DBSelectInterface getRecord() {
        final int humidityDataSize = humidityRecords.size();
        return humidityDataSize > 0
                ? humidityRecords.get(humidityDataSize - 1)
                : null;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        return humidityRecords;
    }

    @Override
    public String getLabel() {
        return DBSelectHumidityData.CSV_HEADER;
    }

    public static final Parcelable.Creator<DBSelectHumidity> CREATOR = new Creator<DBSelectHumidity>() {
        @Override
        public DBSelectHumidity createFromParcel(Parcel source) {
            return new DBSelectHumidity(source);
        }

        @Override
        public DBSelectHumidity[] newArray(int size) {
            return new DBSelectHumidity[size];
        }
    };
}
