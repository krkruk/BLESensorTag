package pl.projektorion.krzysztof.blesensortag.database;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;


import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattModels;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattProfiles;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBParamData;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertParamInterface;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBRootInsertInterface;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertRootRecord;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertFactory;
import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableFactoryInterface;
import pl.projektorion.krzysztof.blesensortag.factories.DBFactoryInserts;
import pl.projektorion.krzysztof.blesensortag.utils.path.PathExternal;
import pl.projektorion.krzysztof.blesensortag.utils.path.PathInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBRootTableRecord;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableFactory;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;
import pl.projektorion.krzysztof.blesensortag.factories.DBFactoryParamInserts;
import pl.projektorion.krzysztof.blesensortag.factories.DBFactoryParamTables;
import pl.projektorion.krzysztof.blesensortag.factories.DBFactoryTables;

public class DBService extends Service {

    public static final String ACTION_SQL_INIT_ERROR =
            "pl.projektorion.krzysztof.blesensortag.database.action.SQL_INIT_ERROR";

    private IBinder binder = new DBServiceBinder();
    private DBTableFactory dbTableFactory = new DBFactoryTables();
    private DBTableFactory dbTableParamFactory = new DBFactoryParamTables();
    private SQLiteOpenHelper dbHelper;
    private List<DBTableInterface> dbTables;
    private List<Observer> dbRows;

    private DBRowWriter dbWriter;
    private SQLiteDatabase db;

    private DBInsertFactory dbInsertFactory;
    private DBInsertFactory dbInsertParamFactory;

    protected BLeAvailableGattProfiles profiles;
    protected BLeAvailableGattModels models;

    protected LocalBroadcastManager broadcaster;

    public DBService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    /**
     * Set all discovered profiles to be then processed by the database system
     * @param profiles {@link BLeAvailableGattProfiles} discovered profiles
     */
    public void setProfiles(BLeAvailableGattProfiles profiles)
    {
        this.profiles = profiles;
    }

    /**
     * Set all discovered models to be then processed by the database system
     * @param models {@link BLeAvailableGattModels} discovered models
     */
    public void setModels(BLeAvailableGattModels models)
    {
        this.models = models;
    }

    /**
     * Set a factory of DBInsert(sensor) classes. Required only if
     * the default set of factories has to be changed
     * @param factory {@link DBFactoryInserts} Factory that contains all DBInsert*Factories
     */
    public void setInsertsFactory(DBInsertFactory factory)
    {
        dbInsertFactory = factory;
    }

    /**
     * Set a factory of DBInsert(sensor)Param classes. Required only if
     * the default set of factories has to be changed.
     * @param factory {@link DBFactoryParamInserts} DBInsert(sensor)Param factory
     */
    public void setInsertParamsFactory(DBInsertFactory factory)
    {
        dbInsertParamFactory = factory;
    }

    public void initService() throws NullPointerException
    {
        if( profiles == null || models == null )
            throw new NullPointerException("No data passed into DBService");

        init_create_tables();
        init_database(models);
        init_insert_params(profiles);
    }

    protected void init_create_tables()
    {
        if( dbTables != null ) return;

        dbTables = new ArrayList<>();
        dbTables.add(new DBRootTableRecord());

        for(DBTableFactoryInterface tableFactory : dbTableFactory)
            dbTables.add(tableFactory.createTable());
        for (DBTableFactoryInterface tableParam : dbTableParamFactory)
            dbTables.add(tableParam.createTable());
    }

    protected void init_database(BLeAvailableGattModels models) throws NullPointerException
    {
        if( dbTables == null )
            throw new NullPointerException("Gatt Services not initialized in DB");

        final PathInterface dbPath = new PathExternal(Constant.DB_NAME, Constant.DB_APP_DIR);
        Log.i("DBPATH", dbPath.getFull());

        try {
            dbHelper = new DBHelper(
                    this,
                    dbPath,
                    dbTables);
            db = dbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            Log.d("SQL", "Database init error: " + e);
            broadcaster.sendBroadcast(new Intent(ACTION_SQL_INIT_ERROR));
            return;
        }

        final DBRootInsertInterface root = new DBInsertRootRecord(db, DBRootTableRecord.TABLE_NAME);
        final long rootRowId = root.getRootRowId();
        dbWriter = new DBRowWriter(db, rootRowId);

        init_row_factory(dbWriter);
        register_observers(models);
    }

    protected void init_insert_params(BLeAvailableGattProfiles profiles)
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
        if( dbInsertFactory == null )
            dbInsertFactory = new DBFactoryInserts(dbWriter);

        if( dbInsertParamFactory == null )
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
