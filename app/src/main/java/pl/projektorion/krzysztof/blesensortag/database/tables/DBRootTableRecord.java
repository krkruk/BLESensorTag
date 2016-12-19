package pl.projektorion.krzysztof.blesensortag.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBRootTableRecord implements DBTableInterface {
    public static final String TABLE_NAME = "Record";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_DATE = "DATE";

    private static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DATE + " INTEGER NOT NULL);";

    public DBRootTableRecord() {
    }

    @Override
    public void createTable(SQLiteDatabase db)
    {
        if( db == null ) return;
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getRootReference() {
        return null;
    }

    @Override
    public String getIdReference() {
        return COLUMN_ID;
    }
}
