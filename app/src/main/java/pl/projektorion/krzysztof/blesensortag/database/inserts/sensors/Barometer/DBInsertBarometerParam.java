package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Barometer;

import android.content.ContentValues;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertParamDataInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Barometer.DBTableBarometerParam;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBInsertBarometerParam extends DBInsertParamAbstract {
    public DBInsertBarometerParam(DBRowWriter dbWriter) {
        super(dbWriter, DBTableBarometerParam.TABLE_NAME);
    }

    @Override
    protected ContentValues values(DBInsertParamDataInterface data) {
        ContentValues values = new ContentValues();
        final int period = (Integer)
                data.getData(DBInsertParamDataInterface.NOTIFY_INTERVAL_PARAMETER);
        values.put(DBTableBarometerParam.NOTIFY_INTERVAL, period);
        return values;
    }
}
