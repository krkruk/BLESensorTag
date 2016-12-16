package pl.projektorion.krzysztof.blesensortag.database;

import android.app.Service;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;


import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattModels;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRootRowInterface;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRootRowRecord;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRowFactory;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowBarometerFactory;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowHumidityFactory;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowIRTemperatureFactory;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowMovementFactory;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowOpticalSensorFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBRootTableRecord;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableFactory;

import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Barometer.DBTableBarometer;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Barometer.DBTableBarometerFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Barometer.DBTableBarometerParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity.DBTableHumidity;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity.DBTableHumidityFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity.DBTableHumidityParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature.DBTableIRTemperature;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature.DBTableIRTemperatureFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.IRTemperature.DBTableIRTemperatureParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovement;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovementFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovementParamFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor.DBTableOpticalSensor;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor.DBTableOpticalSensorFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor.DBTableOpticalSensorParamFactory;

public class DBService extends Service {

    private IBinder binder = new DBServiceBinder();
    private SQLiteOpenHelper dbHelper;
    private DBTableFactory dbTableFactory;
    private DBTableFactory dbTableParamFactory;
    private List<DBTableInterface> dbTables;
    private List<Observer> dbRows;
    private DBRowWriter dbWriter;

    private DBRowFactory dbRowFactory;

    public DBService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void initGattServices(List<BluetoothGattService> services)
    {
        if( dbTables != null ) return;

        init_table_factory();
        dbTables = new ArrayList<>();
        dbTables.add(new DBRootTableRecord());

        for(BluetoothGattService service : services)
        {
            final UUID serviceUuid = service.getUuid();
            dbTables.add(dbTableFactory.createTable(serviceUuid));
            dbTables.add(dbTableParamFactory.createTable(serviceUuid));
        }
    }

    public void initDatabase(BLeAvailableGattModels models) throws NullPointerException
    {
        if( dbTables == null )
            throw new NullPointerException("Gatt Services not initialized in DB");

        dbHelper = new DBHelper(this, dbTables);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        final DBRootRowInterface root = new DBRootRowRecord(db, DBRootTableRecord.TABLE_NAME);
        final long rootRowId = root.getRootRowId();
        dbWriter = new DBRowWriter(db, rootRowId);

        init_row_factory(dbWriter);
        register_observers(models);
    }

    public void write()
    {
        dbWriter.write();
    }

    private void init_table_factory()
    {
        dbTableFactory = new DBTableFactory();
        dbTableFactory.add(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new DBTableBarometerFactory());
        dbTableFactory.add(HumidityProfile.HUMIDITY_SERVICE, new DBTableHumidityFactory());
        dbTableFactory.add(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new DBTableIRTemperatureFactory());
        dbTableFactory.add(MovementProfile.MOVEMENT_SERVICE, new DBTableMovementFactory());
        dbTableFactory.add(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new DBTableOpticalSensorFactory());

        dbTableParamFactory = new DBTableFactory();
        dbTableParamFactory.add(BarometricPressureProfile.BAROMETRIC_PRESSURE_SERVICE,
                new DBTableBarometerParamFactory());
        dbTableParamFactory.add(HumidityProfile.HUMIDITY_SERVICE,
                new DBTableHumidityParamFactory());
        dbTableParamFactory.add(IRTemperatureProfile.IR_TEMPERATURE_SERVICE,
                new DBTableIRTemperatureParamFactory());
        dbTableParamFactory.add(MovementProfile.MOVEMENT_SERVICE,
                new DBTableMovementParamFactory());
        dbTableParamFactory.add(OpticalSensorProfile.OPTICAL_SENSOR_SERVICE,
                new DBTableOpticalSensorParamFactory());
    }

    private void init_row_factory(DBRowWriter dbWriter)
    {
        dbRowFactory = new DBRowFactory();
        dbRowFactory.add(BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA,
                new DBRowBarometerFactory(dbWriter, DBTableBarometer.TABLE_NAME));
        dbRowFactory.add(HumidityProfile.HUMIDITY_DATA,
                new DBRowHumidityFactory(dbWriter, DBTableHumidity.TABLE_NAME));
        dbRowFactory.add(IRTemperatureProfile.IR_TEMPERATURE_DATA,
                new DBRowIRTemperatureFactory(dbWriter, DBTableIRTemperature.TABLE_NAME));
        dbRowFactory.add(MovementProfile.MOVEMENT_DATA,
                new DBRowMovementFactory(dbWriter, DBTableMovement.TABLE_NAME));
        dbRowFactory.add(OpticalSensorProfile.OPTICAL_SENSOR_DATA,
                new DBRowOpticalSensorFactory(dbWriter, DBTableOpticalSensor.TABLE_NAME));
    }

    private void register_observers(BLeAvailableGattModels models)
    {
        dbRows = new ArrayList<>();

        for(UUID modelUuid : models.keySet())
        {
            Observable model = (Observable) models.get(modelUuid);
            Observer dbRow = (Observer) dbRowFactory.createRow(modelUuid);
            model.addObserver(dbRow);
            dbRows.add(dbRow);
        }
    }

    public class DBServiceBinder extends Binder {
        public DBService getService() {
            return DBService.this;
        }
    }
}
