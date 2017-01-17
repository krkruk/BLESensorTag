package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Movement;

import android.content.ContentValues;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBParamDataInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovementParam;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBInsertMovementParam extends DBInsertParamAbstract {
    protected DBInsertMovementParam(DBRowWriter dbWriter) {
        super(dbWriter, DBTableMovementParam.TABLE_NAME);
    }

    @Override
    protected ContentValues values(DBParamDataInterface data) {
        ContentValues values = new ContentValues();
        final double period = (double)
                data.getData(DBParamDataInterface.NOTIFY_INTERVAL_PARAMETER);
        values.put(DBTableMovementParam.NOTIFY_INTERVAL, period);
        values.put(DBTableMovementParam.COLUMN_ROOT_REF, dbWriter.getRootRowId());
        return values;
    }
}
