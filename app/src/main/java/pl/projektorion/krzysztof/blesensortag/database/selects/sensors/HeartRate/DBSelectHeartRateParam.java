package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.HeartRate;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.AppContext;
import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.HeartRate.DBTableHeartRateParam;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBSelectHeartRateParam extends DBSelectSensorParamAbstract {
    private DBSelectHeartRateParamData heartRateParamData;

    public DBSelectHeartRateParam(DBSelectInterface rootRecord) {
        super(rootRecord);
        heartRateParamData = new DBSelectHeartRateParamData();
    }

    @Override
    protected String column_id() {
        return DBTableHeartRateParam.COLUMN_ID;
    }

    @Override
    protected String column_root_ref() {
        return DBTableHeartRateParam.COLUMN_ROOT_REF;
    }

    @Override
    protected String column_notify_interval() {
        return DBTableHeartRateParam.NOTIFY_INTERVAL;
    }

    @Override
    protected String table_name() {
        return DBTableHeartRateParam.TABLE_NAME;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        heartRateParamData = new DBSelectHeartRateParamData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return heartRateParamData;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> data = new ArrayList<>();
        data.add(heartRateParamData);
        return data;
    }

    @Override
    public String getLabel() {
        return AppContext.getContext().getString(R.string.label_heart_rate);
    }
}
