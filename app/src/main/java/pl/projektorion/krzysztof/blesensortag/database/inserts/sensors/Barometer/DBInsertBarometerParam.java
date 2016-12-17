package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Barometer;

import android.content.ContentValues;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBParamDataInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Barometer.DBTableBarometerParam;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBInsertBarometerParam extends DBInsertParamAbstract {
    public DBInsertBarometerParam(DBRowWriter dbWriter) {
        super(dbWriter, DBTableBarometerParam.TABLE_NAME);
    }

    @Override
    protected ContentValues values(DBParamDataInterface data) {
        ContentValues values = new ContentValues();
        final int period = (Integer)
                data.getData(DBParamDataInterface.NOTIFY_INTERVAL_PARAMETER);
        values.put(DBTableBarometerParam.NOTIFY_INTERVAL, period);
        values.put(DBTableBarometerParam.COLUMN_ROOT_REF, dbWriter.getRootRowId());
        return values;
    }
}
