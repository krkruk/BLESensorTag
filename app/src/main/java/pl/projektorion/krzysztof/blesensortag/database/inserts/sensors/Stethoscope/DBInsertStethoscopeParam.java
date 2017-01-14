package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Stethoscope;

import android.content.ContentValues;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBParamDataInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope.DBTableStethoscope;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope.DBTableStethoscopeParam;

/**
 * Created by krzysztof on 14.01.17.
 */

public class DBInsertStethoscopeParam extends DBInsertParamAbstract {
    protected DBInsertStethoscopeParam(DBRowWriter dbWriter) {
        super(dbWriter, DBTableStethoscope.TABLE_NAME);
    }

    @Override
    protected ContentValues values(DBParamDataInterface data) {
        ContentValues values = new ContentValues();
        final int period = (Integer)
                data.getData(DBParamDataInterface.NOTIFY_INTERVAL_PARAMETER);
        values.put(DBTableStethoscopeParam.NOTIFY_INTERVAL, period);
        values.put(DBTableStethoscopeParam.COLUMN_ROOT_REF, dbWriter.getRootRowId());
        return values;
    }
}
