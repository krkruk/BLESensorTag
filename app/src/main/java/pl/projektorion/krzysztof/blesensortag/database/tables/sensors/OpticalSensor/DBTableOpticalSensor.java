package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.OpticalSensor;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBTableOpticalSensor implements DBTableInterface {

    public static final String TABLE_NAME = "OpticalSensor";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_ROOT_REF = "ID_RECORD";
    public static final String COLUMN_LIGHT_INTENSITY = "Intensity";

    private static final String CREATE_TABLE = "CREATE TABLE " +
            TABLE_NAME + " ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ROOT_REF + " INTEGER NOT NULL, " +
            COLUMN_LIGHT_INTENSITY + " REAL NOT NULL, " +
            "FOREIGN KEY( ID_RECORD ) REFERENCES Record( _id ) );";


    @Override
    public void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
