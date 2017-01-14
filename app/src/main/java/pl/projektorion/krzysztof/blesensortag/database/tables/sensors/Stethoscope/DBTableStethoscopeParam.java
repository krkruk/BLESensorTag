package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

/**
 * Created by krzysztof on 14.01.17.
 */

public class DBTableStethoscopeParam implements DBTableInterface {

    public static final String TABLE_NAME = "StethoscopeParam";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_ROOT_REF = "ID_RECORD";
    public static final String NOTIFY_INTERVAL = "NotifyInterval";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_ROOT_REF + " INTEGER NOT NULL," +
                    NOTIFY_INTERVAL + " INTEGER NOT NULL," +
                    "FOREIGN KEY( ID_RECORD ) REFERENCES Record( _id ) );";

    @Override
    public void createTable(SQLiteDatabase db) {
        if( db != null )
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
