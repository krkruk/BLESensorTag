package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Humidity;

import android.content.ContentValues;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertParamDataInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity.DBTableHumidityParam;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBInsertHumidityParam extends DBInsertParamAbstract {
    public DBInsertHumidityParam(DBRowWriter dbWriter) {
        super(dbWriter, DBTableHumidityParam.TABLE_NAME);
    }

    @Override
    protected ContentValues values(DBInsertParamDataInterface data) {
        ContentValues values = new ContentValues();
        final int period = (Integer)
                data.getData(DBInsertParamDataInterface.NOTIFY_INTERVAL_PARAMETER);
        values.put(DBTableHumidityParam.TABLE_NAME, period);

        return values;
    }
}
