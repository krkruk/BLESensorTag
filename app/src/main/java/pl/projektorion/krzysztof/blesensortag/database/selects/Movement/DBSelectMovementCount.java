package pl.projektorion.krzysztof.blesensortag.database.selects.Movement;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorCountAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovement;

/**
 * Created by krzysztof on 25.12.16.
 */

public class DBSelectMovementCount extends DBSelectSensorCountAbstract {
    private DBSelectInterface movementRecordsCount;

    public DBSelectMovementCount(DBSelectInterface rootRecord) {
        super(rootRecord);
        this.movementRecordsCount = new DBSelectMovementCountData();
    }

    public DBSelectMovementCount(Parcel in) {
        super(in);
        movementRecordsCount = in.readParcelable(DBSelectInterface.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(movementRecordsCount, flags);
    }

    @Override
    public String getLabel() {
        return DBSelectMovementCountData.CSV_HEADER;
    }

    @Override
    protected String table_name() {
        return DBTableMovement.TABLE_NAME;
    }

    @Override
    protected String grouping_column() {
        return DBTableMovement.COLUMN_ROOT_REF;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        movementRecordsCount = new DBSelectMovementCountData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return movementRecordsCount;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> data = new ArrayList<>();
        data.add(movementRecordsCount);
        return data;
    }

    public static final Parcelable.Creator<DBSelectMovementCount> CREATOR = new Creator<DBSelectMovementCount>() {
        @Override
        public DBSelectMovementCount createFromParcel(Parcel source) {
            return new DBSelectMovementCount(source);
        }

        @Override
        public DBSelectMovementCount[] newArray(int size) {
            return new DBSelectMovementCount[size];
        }
    };
}
