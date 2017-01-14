package pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.AppContext;
import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.abstracts.DBSelectSensorParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope.DBTableStethoscopeParam;

/**
 * Created by krzysztof on 15.01.17.
 */

public class DBSelectStethoscopeParam extends DBSelectSensorParamAbstract {
    private DBSelectInterface stethoscopeParam;

    public DBSelectStethoscopeParam(DBSelectInterface rootRecord) {
        super(rootRecord);
        stethoscopeParam = new DBSelectStethoscopeParamData();
    }

    @Override
    protected String column_id() {
        return DBTableStethoscopeParam.COLUMN_ID;
    }

    @Override
    protected String column_root_ref() {
        return DBTableStethoscopeParam.COLUMN_ROOT_REF;
    }

    @Override
    protected String column_notify_interval() {
        return DBTableStethoscopeParam.NOTIFY_INTERVAL;
    }

    @Override
    protected String table_name() {
        return DBTableStethoscopeParam.TABLE_NAME;
    }

    @Override
    public void onQueryExecuted(Cursor cursor) {
        stethoscopeParam = new DBSelectStethoscopeParamData(cursor);
    }

    @Override
    public DBSelectInterface getRecord() {
        return stethoscopeParam;
    }

    @Override
    public List<? extends DBSelectInterface> getRecords() {
        List<DBSelectInterface> data = new ArrayList<>();
        data.add(stethoscopeParam);
        return data;
    }

    @Override
    public String getLabel() {
        return AppContext.getContext().getString(R.string.label_stethoscope_sensor);
    }
}
