package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Humidity;

import android.content.ContentValues;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBParamDataInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity.DBTableHumidityParam;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBInsertHumidityParam extends DBInsertParamAbstract {
    public DBInsertHumidityParam(DBRowWriter dbWriter) {
        super(dbWriter, DBTableHumidityParam.TABLE_NAME);
    }

    @Override
    protected ContentValues values(DBParamDataInterface data) {
        ContentValues values = new ContentValues();
        final double period = (double)
                data.getData(DBParamDataInterface.NOTIFY_INTERVAL_PARAMETER);
        values.put(DBTableHumidityParam.NOTIFY_INTERVAL, period);
        values.put(DBTableHumidityParam.COLUMN_ROOT_REF, dbWriter.getRootRowId());
        return values;
    }
}
