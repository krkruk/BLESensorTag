package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.OpticalSensor;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.AppContext;
import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor.DBTableOpticalSensorParam;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBSelectOpticalSensorParam extends DBSelectSensorParamAbstract {
    private DBSelectInterface opticalSensorRecord;

    public DBSelectOpticalSensorParam(DBSelectInterface rootRecord) {
        super(rootRecord);
        opticalSensorRecord = new DBSelectOpticalSensorParamData();
    }

    @Override
    protected String column_id() {
        return DBTableOpticalSensorParam.COLUMN_ID;
    }

    @Override
    protected String column_root_ref() {
        return DBTableOpticalSensorParam.COLUMN_ROOT_REF;
    }

    @Override
    protected String column_notify_interval() {
        return DBTableOpticalSensorParam.NOTIFY_INTERVAL;
    }

    @Override
    protected String table_name() {
        return DBTableOpticalSensorParam.TABLE_NAME;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        opticalSensorRecord = new DBSelectOpticalSensorParamData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return opticalSensorRecord;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> records = new ArrayList<>();
        records.add(opticalSensorRecord);
        return records;
    }

    @Override
    public String getLabel() {
        return AppContext.getContext().getString(R.string.label_light_intensity_sensor);
    }
}
