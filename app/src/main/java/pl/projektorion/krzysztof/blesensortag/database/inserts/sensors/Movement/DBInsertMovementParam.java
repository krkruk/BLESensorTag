package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Movement;

import android.content.ContentValues;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertParamDataInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovementParam;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBInsertMovementParam extends DBInsertParamAbstract {
    protected DBInsertMovementParam(DBRowWriter dbWriter) {
        super(dbWriter, DBTableMovementParam.TABLE_NAME);
    }

    @Override
    protected ContentValues values(DBInsertParamDataInterface data) {
        ContentValues values = new ContentValues();
        final int period = (Integer)
                data.getData(DBInsertParamDataInterface.NOTIFY_INTERVAL_PARAMETER);
        values.put(DBTableMovementParam.TABLE_NAME, period);

        return values;
    }
}
