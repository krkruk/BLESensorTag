package pl.projektorion.krzysztof.blesensortag.database.selects.Humidity;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.constants.ProfileName;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectSensorParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity.DBTableHumidityParam;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectHumidityParam extends DBSelectSensorParamAbstract {
    private DBSelectInterface humidityRecord;

    public DBSelectHumidityParam(DBSelectInterface rootRecord) {
        super(rootRecord);
        humidityRecord = new DBSelectHumidityParamData();
    }

    @Override
    protected String column_id() {
        return DBTableHumidityParam.COLUMN_ID;
    }

    @Override
    protected String column_root_ref() {
        return DBTableHumidityParam.COLUMN_ROOT_REF;
    }

    @Override
    protected String column_notify_interval() {
        return DBTableHumidityParam.NOTIFY_INTERVAL;
    }

    @Override
    protected String table_name() {
        return DBTableHumidityParam.TABLE_NAME;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        humidityRecord = new DBSelectHumidityParamData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return humidityRecord;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> records = new ArrayList<>();
        records.add(humidityRecord);
        return records;
    }

    @Override
    public String getLabel() {
        return ProfileName.HUMIDITY_PROFILE;
    }
}
