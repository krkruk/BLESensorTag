package pl.projektorion.krzysztof.blesensortag.database;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementData;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRootRowInterface;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRootRowRecord;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowBarometer;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowHumidity;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowIRTemperature;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowMovement;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowOpticalSensor;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBRootTableRecord;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableBarometer;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableHumidity;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableIRTemperature;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableMovement;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableOpticalSensor;

public class DBService extends Service {

    private IBinder binder = new DBServiceBinder();
    private SQLiteOpenHelper dbHelper;

    public DBService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void initDatabase()
    {
        List<DBTableInterface> tables = getTables();
        dbHelper = new DBHelper(this, tables);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        final DBRootRowInterface root = new DBRootRowRecord(db, DBRootTableRecord.TABLE_NAME);
        final long rootRowId = root.getRootRowId();
        final DBRowWriter dbWriter = new DBRowWriter(db, rootRowId);
        final DBRowBarometer barometer = new DBRowBarometer(dbWriter,
                DBTableBarometer.TABLE_NAME);
        final DBRowHumidity humidity = new DBRowHumidity(dbWriter, DBTableHumidity.TABLE_NAME);
        final DBRowIRTemperature irTemp = new DBRowIRTemperature(dbWriter, DBTableIRTemperature.TABLE_NAME);
        final DBRowMovement movement = new DBRowMovement(dbWriter, DBTableMovement.TABLE_NAME);
        final DBRowOpticalSensor opticalSensor = new DBRowOpticalSensor(dbWriter, DBTableOpticalSensor.TABLE_NAME);


        barometer.update(null, new BarometricPressureData(new byte[] {0,0,0,0,0,0}));
        barometer.update(null, new BarometricPressureData(new byte[] {1,1,1,1,1,1}));
        barometer.update(null, new BarometricPressureData(new byte[] {2,2,2,2,2,2}));

        humidity.update(null, new HumidityData(new byte[]{0,0,0,0}));
        humidity.update(null, new HumidityData(new byte[]{2,2,2,2}));

        irTemp.update(null, new IRTemperatureData(new byte[] {0,0,0,0}));
        irTemp.update(null, new IRTemperatureData(new byte[] {2,2,2,2}));

        movement.update(null, new MovementData(new byte[] {1,1,1, 1,1,1, 1,1,1, 1,1,1, 1,1,1, 1,1,1}));
        movement.update(null, new MovementData(new byte[] {1,1,1, 1,1,1, 1,1,1, 1,1,1, 1,1,1, 1,1,1}));

        opticalSensor.update(null, new HumidityData(new byte[]{0,0,0,0}));
        opticalSensor.update(null, new HumidityData(new byte[]{2,2,2,2}));


        dbWriter.write();
        db.close();
    }

    private List<DBTableInterface> getTables()
    {
        List<DBTableInterface> tables = new ArrayList<>();
        tables.add(new DBRootTableRecord());
        tables.add(new DBTableBarometer());
        tables.add(new DBTableHumidity());
        tables.add(new DBTableIRTemperature());
        tables.add(new DBTableMovement());
        tables.add(new DBTableOpticalSensor());

        return tables;
    }

    public class DBServiceBinder extends Binder {
        public DBService getService() {
            return DBService.this;
        }
    }
}
