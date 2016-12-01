package pl.projektorion.krzysztof.blesensortag.database.rows.sensors;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notify.ProfileData;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriteCommand;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRowAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableHumidity;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBRowHumidity extends DBRowAbstract {
    private Observable observable;


    public DBRowHumidity(DBRowWriter dbWriter, long rootRowId, String tableName) {
        super(dbWriter, rootRowId, tableName);
    }

    public DBRowHumidity(DBRowWriter dbWriter, String tableName) {
        super(dbWriter, tableName);
    }

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        ProfileData data = (ProfileData) arg;
        final double humidity = data.getValue(HumidityData.ATTRIBUTE_RELATIVE_HUMIDITY);
        final double temperature = data.getValue(HumidityData.ATTRIBUTE_TEMPERATURE_CELSIUS);

        ContentValues dbValues = new ContentValues();
        dbValues.put(DBTableHumidity.COLUMN_ROOT_REF, rootRowId);
        dbValues.put(DBTableHumidity.COLUMN_REL_HUMIDITY, humidity);
        dbValues.put(DBTableHumidity.COLUMN_TEMPERATURE, temperature);

        final SQLiteDatabase db = dbWriter.getWritableDatabase();
        dbWriter.add(new DBRowWriteCommand(db, getTableName(), dbValues));
    }
}
