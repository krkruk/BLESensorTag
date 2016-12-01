package pl.projektorion.krzysztof.blesensortag.database;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.BarometricPressure.BarometricPressureData;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRootRowInterface;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRootRowRecord;
import pl.projektorion.krzysztof.blesensortag.database.rows.DBRowBarometer;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBRootTableRecord;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableBarometer;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableInterface;

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
        final DBRowWriter dbWriter = new DBRowWriter(db);
        final DBRowBarometer barometer = new DBRowBarometer(dbWriter, rootRowId,
                DBTableBarometer.TABLE_NAME);

        barometer.update(null, new BarometricPressureData(new byte[] {0,0,0,0,0,0}));
        barometer.update(null, new BarometricPressureData(new byte[] {1,1,1,1,1,1}));
        barometer.update(null, new BarometricPressureData(new byte[] {2,2,2,2,2,2}));

        dbWriter.write();
        db.close();
    }

    private List<DBTableInterface> getTables()
    {
        List<DBTableInterface> tables = new ArrayList<>();
        tables.add(new DBRootTableRecord());
        tables.add(new DBTableBarometer());

        return tables;
    }

    public class DBServiceBinder extends Binder {
        public DBService getService() {
            return DBService.this;
        }
    }
}
