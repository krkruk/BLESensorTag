package pl.projektorion.krzysztof.blesensortag.database.rows.sensors;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileData;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriteCommand;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRowAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableOpticalSensor;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBRowOpticalSensor extends DBRowAbstract {

    private Observable observable;


    public DBRowOpticalSensor(DBRowWriter dbWriter, long rootRowId, String tableName) {
        super(dbWriter, rootRowId, tableName);
    }

    public DBRowOpticalSensor(DBRowWriter dbWriter, String tableName) {
        super(dbWriter, tableName);
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
