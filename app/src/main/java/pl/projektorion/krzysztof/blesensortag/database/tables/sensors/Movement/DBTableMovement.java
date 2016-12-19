package pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBTableMovement implements DBTableInterface {

    public static final String TABLE_NAME = "Movement";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_ROOT_REF = "ID_RECORD";
    public static final String COLUMN_ACC_X = "Acc_X";
    public static final String COLUMN_ACC_Y = "Acc_Y";
    public static final String COLUMN_ACC_Z = "Acc_Z";
    public static final String COLUMN_GYR_X = "Gyr_X";
    public static final String COLUMN_GYR_Y = "Gyr_Y";
    public static final String COLUMN_GYR_Z = "Gyr_Z";
    public static final String COLUMN_MAG_X = "Mag_X";
    public static final String COLUMN_MAG_Y = "Mag_Y";
    public static final String COLUMN_MAG_Z = "Mag_Z";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_ROOT_REF + " INTEGER NOT NULL, " +
            COLUMN_ACC_X + " REAL NOT NULL, " +
            COLUMN_ACC_Y + " REAL NOT NULL, " +
            COLUMN_ACC_Z + " REAL NOT NULL, " +
            COLUMN_GYR_X + " REAL NOT NULL, " +
            COLUMN_GYR_Y + " REAL NOT NULL, " +
            COLUMN_GYR_Z + " REAL NOT NULL, " +
            COLUMN_MAG_X + " REAL NOT NULL, " +
            COLUMN_MAG_Y + " REAL NOT NULL, " +
            COLUMN_MAG_Z + " REAL NOT NULL, " +
            "FOREIGN KEY( ID_RECORD ) REFERENCES Record( _id ));";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
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
