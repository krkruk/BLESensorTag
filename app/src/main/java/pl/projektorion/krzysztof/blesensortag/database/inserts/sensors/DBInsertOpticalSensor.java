package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileData;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriteCommand;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor.DBTableOpticalSensor;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBInsertOpticalSensor extends DBInsertAbstract {

    private Observable observable;


    public DBInsertOpticalSensor(DBRowWriter dbWriter, long rootRowId) {
        super(dbWriter, rootRowId, DBTableOpticalSensor.TABLE_NAME);
    }

    public DBInsertOpticalSensor(DBRowWriter dbWriter) {
        super(dbWriter, DBTableOpticalSensor.TABLE_NAME);
    }

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileData data = (ProfileData) arg;
        final double lightIntensity = data.getValue(OpticalSensorData.ATTRIBUTE_LIGHT_INTENSITY_LUX);

        ContentValues dbValues = new ContentValues();
        dbValues.put(DBTableOpticalSensor.COLUMN_ROOT_REF, rootRowId);
        dbValues.put(DBTableOpticalSensor.COLUMN_LIGHT_INTENSITY, lightIntensity);

        final SQLiteDatabase db = dbWriter.getWritableDatabase();
        dbWriter.add(new DBRowWriteCommand(db, getTableName(), dbValues));
    }
}
