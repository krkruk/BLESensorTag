package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.OpticalSensor;

import android.content.ContentValues;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertParamAbstract;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertParamDataInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor.DBTableOpticalSensorParam;

/**
 * Created by krzysztof on 16.12.16.
 */

public class DBInsertOpticalSensorParam extends DBInsertParamAbstract {
    protected DBInsertOpticalSensorParam(DBRowWriter dbWriter) {
        super(dbWriter, DBTableOpticalSensorParam.TABLE_NAME);
    }

    @Override
    protected ContentValues values(DBInsertParamDataInterface data) {
        ContentValues values = new ContentValues();
        final int period = (Integer)
                data.getData(DBInsertParamDataInterface.NOTIFY_INTERVAL_PARAMETER);
        values.put(DBTableOpticalSensorParam.NOTIFY_INTERVAL, period);
        values.put(DBTableOpticalSensorParam.COLUMN_ROOT_REF, dbWriter.getRootRowId());
        return values;
    }
}
