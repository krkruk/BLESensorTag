package pl.projektorion.krzysztof.blesensortag.database;

import android.app.Service;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattModels;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRootRowInterface;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRootRowRecord;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRowFactory;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowBarometer;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowBarometerFactory;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowHumidity;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowHumidityFactory;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowIRTemperature;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowIRTemperatureFactory;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowMovement;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowMovementFactory;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowOpticalSensor;
import pl.projektorion.krzysztof.blesensortag.database.rows.sensors.DBRowOpticalSensorFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBRootTableRecord;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableBarometer;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableBarometerFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableHumidity;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableHumidityFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableIRTemperature;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableIRTemperatureFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableMovement;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableMovementFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableOpticalSensor;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.DBTableOpticalSensorFactory;

public class DBService extends Service {

    private IBinder binder = new DBServiceBinder();
    private SQLiteOpenHelper dbHelper;
    private DBTableFactory dbTableFactory;
    private List<DBTableInterface> dbTables;
    private List<Observer> dbRows;

    private DBRowFactory dbRowFactory;

    private final Executor executor = Executors.newSingleThreadExecutor();
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
        final DBRowWriter dbWriter = new DBRowWriter(db, rootRowId);

        init_row_factory(dbWriter);
        register_observers(models);
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
