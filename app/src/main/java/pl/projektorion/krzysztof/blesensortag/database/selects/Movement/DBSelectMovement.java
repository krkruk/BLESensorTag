package pl.projektorion.krzysztof.blesensortag.database.selects.Movement;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovement;

/**
 * Created by krzysztof on 22.12.16.
 */

public class DBSelectMovement extends DBSelectSensorAbstract {
    private List<DBSelectMovementData> movementRecords;

    public DBSelectMovement(DBSelectInterface rootRecord, DBSelectInterface sensorRecord) {
        super(rootRecord, sensorRecord);
        movementRecords = new ArrayList<>();
    }

    public DBSelectMovement(Parcel in) {
        super(in);
        movementRecords = new ArrayList<>();
        in.readTypedList(movementRecords, DBSelectMovementData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(movementRecords);
    }

    @Override
    protected void parse_cursor_data(long time, Cursor cursor) {
        movementRecords.add(new DBSelectMovementData(time, cursor));
    }

    @Override
    protected String select_db_columns() {
        return  DBTableMovement.COLUMN_ACC_X + ", " +
                DBTableMovement.COLUMN_ACC_Y + ", " +
                DBTableMovement.COLUMN_ACC_Z + ", " +
                DBTableMovement.COLUMN_GYR_X + ", " +
                DBTableMovement.COLUMN_GYR_Y + ", " +
                DBTableMovement.COLUMN_GYR_Z + ", " +
                DBTableMovement.COLUMN_MAG_X + ", " +
                DBTableMovement.COLUMN_MAG_Y + ", " +
                DBTableMovement.COLUMN_MAG_Z;
    }

    @Override
    protected String select_table_name() {
        return DBTableMovement.TABLE_NAME;
    }

    @Override
    protected String select_extract_records_where() {
        return DBTableMovement.COLUMN_ROOT_REF;
    }

    @Override
    public DBSelectInterface getRecord() {
        final int dataSize = movementRecords.size();
        return dataSize > 0
                ? movementRecords.get(dataSize - 1)
                : null;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        return movementRecords;
    }

    @Override
    public String getLabel() {
        return DBSelectMovementData.CSV_HEADER;
    }

    public static final Parcelable.Creator<DBSelectMovement> CREATOR = new Creator<DBSelectMovement>() {
        @Override
        public DBSelectMovement createFromParcel(Parcel source) {
            return new DBSelectMovement(source);
        }

        @Override
        public DBSelectMovement[] newArray(int size) {
            return new DBSelectMovement[size];
        }
    };
}
