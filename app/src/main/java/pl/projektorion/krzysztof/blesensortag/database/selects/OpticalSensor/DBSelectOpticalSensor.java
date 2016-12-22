package pl.projektorion.krzysztof.blesensortag.database.selects.OpticalSensor;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor.DBTableOpticalSensor;

/**
 * Created by krzysztof on 22.12.16.
 */

public class DBSelectOpticalSensor extends DBSelectSensorAbstract {
    private List<DBSelectOpticalSensorData> opticalSensorRecords;

    public DBSelectOpticalSensor(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        super(rootRecord, sensorRecord);
        this.opticalSensorRecords = new ArrayList<>();
    }

    public DBSelectOpticalSensor(Parcel in) {
        super(in);
        this.opticalSensorRecords = new ArrayList<>();
        in.readTypedList(this.opticalSensorRecords, DBSelectOpticalSensorData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(opticalSensorRecords);
    }

    @Override
    protected void parse_cursor_data(long time, Cursor cursor) {
        opticalSensorRecords.add(new DBSelectOpticalSensorData(time, cursor));
    }

    @Override
    protected String select_db_columns() {
        return DBTableOpticalSensor.COLUMN_LIGHT_INTENSITY;
    }

    @Override
    protected String select_table_name() {
        return DBTableOpticalSensor.TABLE_NAME;
    }

    @Override
    protected String select_extract_records_where() {
        return DBTableOpticalSensor.COLUMN_ROOT_REF;
    }

    @Override
    public DBSelectInterface getRecord() {
        final int dataSize = opticalSensorRecords.size();
        return dataSize > 0
                ? opticalSensorRecords.get(dataSize-1)
                : null;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        return opticalSensorRecords;
    }

    @Override
    public String getLabel() {
        return DBSelectOpticalSensorData.CSV_HEADER;
    }

    public static final Parcelable.Creator<DBSelectOpticalSensor> CREATOR = new Creator<DBSelectOpticalSensor>() {
        @Override
        public DBSelectOpticalSensor createFromParcel(Parcel source) {
            return new DBSelectOpticalSensor(source);
        }

        @Override
        public DBSelectOpticalSensor[] newArray(int size) {
            return new DBSelectOpticalSensor[size];
        }
    };
}
