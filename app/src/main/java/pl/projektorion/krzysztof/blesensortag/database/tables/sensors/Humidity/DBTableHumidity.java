package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Humidity;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBTableHumidity implements DBTableInterface {

    public static final String TABLE_NAME = "Humidity";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_ROOT_REF = "ID_RECORD";
    public static final String COLUMN_REL_HUMIDITY = "RelativeHumidity";
    public static final String COLUMN_TEMPERATURE = "Temperature";

    private static final String CREATE_TABLE = "CREATE TABLE " +
            TABLE_NAME + " ( "+
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COLUMN_ROOT_REF + " INTEGER NOT NULL, "+
            COLUMN_REL_HUMIDITY + " REAL NOT NULL, "+
            COLUMN_TEMPERATURE + " REAL NOT NULL, "+
            "FOREIGN KEY( ID_RECORD ) REFERENCES Record( _id ));";

    @Override
    public void createTable(SQLiteDatabase db) {
        if( db == null ) return;
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
