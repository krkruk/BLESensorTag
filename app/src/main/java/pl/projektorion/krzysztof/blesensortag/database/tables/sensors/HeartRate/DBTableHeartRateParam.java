package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.HeartRate;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableParamInterface;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBTableHeartRateParam implements DBTableParamInterface {

    public static final String TABLE_NAME = "HeartRateParam";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_ROOT_REF = "ID_RECORD";
    public static final String NOTIFY_INTERVAL = "NotifyInterval";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_ROOT_REF + " INTEGER NOT NULL," +
                    NOTIFY_INTERVAL + " REAL NOT NULL," +
                    "FOREIGN KEY( ID_RECORD ) REFERENCES Record( _id ) );";

    @Override
    public void createTable(SQLiteDatabase db) {
        if( db == null ) return;
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getRootReference() {
        return COLUMN_ROOT_REF;
    }

    @Override
    public String getIdReference() {
        return COLUMN_ID;
    }
}
