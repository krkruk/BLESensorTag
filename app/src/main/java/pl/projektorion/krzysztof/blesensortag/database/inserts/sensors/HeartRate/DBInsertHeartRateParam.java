package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.HeartRate;

import android.content.ContentValues;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBParamDataInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.HeartRate.DBTableHeartRateParam;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBInsertHeartRateParam extends DBInsertParamAbstract {
    protected DBInsertHeartRateParam(DBRowWriter dbWriter) {
        super(dbWriter, DBTableHeartRateParam.TABLE_NAME);
    }

    @Override
    protected ContentValues values(DBParamDataInterface data) {
        final Double notifyPeriod = (Double) data
                .getData(DBParamDataInterface.NOTIFY_INTERVAL_PARAMETER);

        ContentValues values = new ContentValues();
        if( notifyPeriod == null ) return values;
        values.put(DBTableHeartRateParam.COLUMN_ROOT_REF, dbWriter.getRootRowId());
        values.put(DBTableHeartRateParam.NOTIFY_INTERVAL, notifyPeriod);

        return values;
    }
}
