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


import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Humidity.HumidityProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.IRTemperature.IRTemperatureProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.OpticalSensor.OpticalSensorProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattModels;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattProfiles;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBParamData;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertParamInterface;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBRootInsertInterface;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertRootRecord;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Barometer.DBInsertBarometerFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Humidity.DBInsertHumidityFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.IRTemperature.DBInsertIRTemperatureFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Movement.DBInsertMovementFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.OpticalSensor.DBInsertOpticalSensorFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBRootTableRecord;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableFactory;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;
import pl.projektorion.krzysztof.blesensortag.factories.DBFactoryParamInserts;
import pl.projektorion.krzysztof.blesensortag.factories.DBFactoryParamTables;
import pl.projektorion.krzysztof.blesensortag.factories.DBFactoryTables;

public class DBService extends Service {

    private IBinder binder = new DBServiceBinder();
    private DBTableFactory dbTableFactory = new DBFactoryTables();
    private DBTableFactory dbTableParamFactory = new DBFactoryParamTables();
    private SQLiteOpenHelper dbHelper;
    private List<DBTableInterface> dbTables;
    private List<Observer> dbRows;
    private DBRowWriter dbWriter;

    private DBInsertFactory dbInsertFactory;
    private DBInsertFactory dbInsertParamFactory;

    private List<BluetoothGattService> services;
    private BLeAvailableGattProfiles profiles;
    private BLeAvailableGattModels models;

    public DBService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setServices(List<BluetoothGattService> services)
    {
        this.services = services;
    }

    public void setProfiles(BLeAvailableGattProfiles profiles)
    {
        this.profiles = profiles;
    }

    public void setModels(BLeAvailableGattModels models)
    {
        this.models = models;
    }

    public void initService() throws NullPointerException
    {
        if( services == null || profiles == null || models == null )
            throw new NullPointerException("No data passed into DBService");

        initGattServices(services);
        initDatabase(models);
        insertParams(profiles);
    }

    protected void initGattServices(List<BluetoothGattService> services)
    {
        if( dbTables != null ) return;

        dbTables = new ArrayList<>();
        dbTables.add(new DBRootTableRecord());

        for(BluetoothGattService service : services)
        {
            final UUID serviceUuid = service.getUuid();
            dbTables.add(dbTableFactory.createTable(serviceUuid));
            dbTables.add(dbTableParamFactory.createTable(serviceUuid));
        }
    }

    protected void initDatabase(BLeAvailableGattModels models) throws NullPointerException
    {
        if( dbTables == null )
            throw new NullPointerException("Gatt Services not initialized in DB");

        dbHelper = new DBHelper(this, dbTables);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        final DBRootInsertInterface root = new DBInsertRootRecord(db, DBRootTableRecord.TABLE_NAME);
        final long rootRowId = root.getRootRowId();
        dbWriter = new DBRowWriter(db, rootRowId);

        init_row_factory(dbWriter);
        register_observers(models);
    }

    protected void insertParams(BLeAvailableGattProfiles profiles)
    {
        for(UUID profileUuid : profiles.keySet())
        {
            DBInsertParamInterface paramTable = (DBInsertParamInterface)
                    dbInsertParamFactory.createRow(profileUuid);

            final GenericGattProfileInterface genericProfile = profiles.get(profileUuid);
            if( !(genericProfile instanceof NotifyGattProfileInterface) )
                continue;
            final NotifyGattProfileInterface profile = (NotifyGattProfileInterface) genericProfile;

            final DBParamData data = new DBParamData(profile);
            paramTable.insert(data);
        }
    }

    public void write()
    {
        dbWriter.write();
    }

    public boolean isEmpty() { return dbWriter.isEmpty(); }

    private void init_row_factory(DBRowWriter dbWriter)
    {
        dbInsertFactory = new DBInsertFactory();
        dbInsertFactory.add(BarometricPressureProfile.BAROMETRIC_PRESSURE_DATA,
                new DBInsertBarometerFactory(dbWriter));
        dbInsertFactory.add(HumidityProfile.HUMIDITY_DATA,
                new DBInsertHumidityFactory(dbWriter));
        dbInsertFactory.add(IRTemperatureProfile.IR_TEMPERATURE_DATA,
                new DBInsertIRTemperatureFactory(dbWriter));
        dbInsertFactory.add(MovementProfile.MOVEMENT_DATA,
                new DBInsertMovementFactory(dbWriter));
        dbInsertFactory.add(OpticalSensorProfile.OPTICAL_SENSOR_DATA,
                new DBInsertOpticalSensorFactory(dbWriter));

        dbInsertParamFactory = new DBFactoryParamInserts(dbWriter);
    }

    private void register_observers(BLeAvailableGattModels models)
    {
        dbRows = new ArrayList<>();

        for(UUID modelUuid : models.keySet())
        {
            Observable model = (Observable) models.get(modelUuid);
            Observer dbRow = (Observer) dbInsertFactory.createRow(modelUuid);
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
