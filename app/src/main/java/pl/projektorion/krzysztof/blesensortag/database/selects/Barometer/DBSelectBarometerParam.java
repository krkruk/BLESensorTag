package pl.projektorion.krzysztof.blesensortag.database.selects.Barometer;

import android.database.Cursor;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Barometer.DBTableBarometerParam;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectBarometerParam extends DBSelectSensorParamAbstract {
    private DBSelectInterface barometerRecord;

    public DBSelectBarometerParam(DBSelectInterface rootRecord) {
        super(rootRecord);
        barometerRecord = new DBSelectBarometerParamData();
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        this.barometerRecord = new DBSelectBarometerParamData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return barometerRecord;
    }

    @Override
    protected String column_id() {
        return DBTableBarometerParam.COLUMN_ID;
    }

    @Override
    protected String column_root_ref() {
        return DBTableBarometerParam.COLUMN_ROOT_REF;
    }

    @Override
    protected String column_notify_interval() {
        return DBTableBarometerParam.NOTIFY_INTERVAL;
    }

    @Override
    protected String table_name() {
        return DBTableBarometerParam.TABLE_NAME;
    }
}
