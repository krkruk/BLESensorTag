package pl.projektorion.krzysztof.blesensortag.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableInterface;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = Constant.DB_NAME;
    private static final int DB_VERSION = 1;

    private List<DBTableInterface> tables;

    public DBHelper(Context context, List<DBTableInterface> tables) {
        super(context, DB_NAME, null, DB_VERSION);
        this.tables = tables;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(DBTableInterface table : tables)
            table.createTable(db);

        Log.i("DB", "Tables created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing - everything is all right
    }
}
