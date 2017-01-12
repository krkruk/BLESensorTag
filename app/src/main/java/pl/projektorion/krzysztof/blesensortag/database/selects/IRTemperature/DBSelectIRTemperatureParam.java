package pl.projektorion.krzysztof.blesensortag.database.selects.IRTemperature;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.AppContext;
import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature.DBTableIRTemperatureParam;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectIRTemperatureParam extends DBSelectSensorParamAbstract {
    private DBSelectInterface irTemperatureRecord;

    public DBSelectIRTemperatureParam(DBSelectInterface rootRecord) {
        super(rootRecord);
        this.irTemperatureRecord = new DBSelectIRTemperatureParamData();
    }

    @Override
    protected String column_id() {
        return DBTableIRTemperatureParam.COLUMN_ID;
    }

    @Override
    protected String column_root_ref() {
        return DBTableIRTemperatureParam.COLUMN_ROOT_REF;
    }

    @Override
    protected String column_notify_interval() {
        return DBTableIRTemperatureParam.NOTIFY_INTERVAL;
    }

    @Override
    protected String table_name() {
        return DBTableIRTemperatureParam.TABLE_NAME;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        irTemperatureRecord = new DBSelectIRTemperatureParamData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return irTemperatureRecord;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> records = new ArrayList<>();
        records.add(irTemperatureRecord);
        return records;
    }

    @Override
    public String getLabel() {
        return AppContext.getContext().getString(R.string.label_temperature_sensor);
    }
}
