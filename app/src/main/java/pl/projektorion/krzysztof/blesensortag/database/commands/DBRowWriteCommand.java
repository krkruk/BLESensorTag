package pl.projektorion.krzysztof.blesensortag.database.commands;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBRowWriteCommand implements DBRowWriteInterface {

    private SQLiteDatabase db;
    private String tableName;
    private ContentValues values;

    public DBRowWriteCommand(SQLiteDatabase db, String tableName, ContentValues values) {
        this.db = db;
        this.tableName = tableName;
        this.values = values;
    }

    @Override
    public void execute() {
        db.insert(tableName, null, values);
    }
}
