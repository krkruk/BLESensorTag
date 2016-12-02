package pl.projektorion.krzysztof.blesensortag.database.tables;

import android.database.sqlite.SQLiteDatabase;

import pl.projektorion.krzysztof.blesensortag.database.tables.DBTableInterface;

/**
 * Created by krzysztof on 02.12.16.
 */

public class DBTableNull implements DBTableInterface {
    @Override
    public void createTable(SQLiteDatabase db) {

    }

    @Override
    public String getTableName() {
        return "";
    }
}
