package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.IRTemperature;

import android.content.ContentValues;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBParamDataInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature.DBTableIRTemperatureParam;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBInsertIRTemperatureParam extends DBInsertParamAbstract {

    public DBInsertIRTemperatureParam(DBRowWriter dbWriter) {
        super(dbWriter, DBTableIRTemperatureParam.TABLE_NAME);
    }

    @Override
    protected ContentValues values(DBParamDataInterface data) {
        ContentValues values = new ContentValues();
        final int period = (Integer)
                data.getData(DBParamDataInterface.NOTIFY_INTERVAL_PARAMETER);
        values.put(DBTableIRTemperatureParam.NOTIFY_INTERVAL, period);
        values.put(DBTableIRTemperatureParam.COLUMN_ROOT_REF, dbWriter.getRootRowId());
        return values;
    }
}
