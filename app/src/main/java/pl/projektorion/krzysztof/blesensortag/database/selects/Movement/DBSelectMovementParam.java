package pl.projektorion.krzysztof.blesensortag.database.selects.Movement;

import android.database.Cursor;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovementParam;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectMovementParam extends DBSelectSensorParamAbstract {
    private DBSelectMovementParamData movementRecord;

    public DBSelectMovementParam(DBSelectInterface rootRecord) {
        super(rootRecord);
        this.movementRecord = new DBSelectMovementParamData();
    }

    @Override
    protected String column_id() {
        return DBTableMovementParam.COLUMN_ID;
    }

    @Override
    protected String column_root_ref() {
        return DBTableMovementParam.COLUMN_ROOT_REF;
    }

    @Override
    protected String column_notify_interval() {
        return DBTableMovementParam.NOTIFY_INTERVAL;
    }

    @Override
    protected String table_name() {
        return DBTableMovementParam.TABLE_NAME;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        movementRecord = new DBSelectMovementParamData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return movementRecord;
    }
}
