package pl.projektorion.krzysztof.blesensortag.database.tables;

import android.database.sqlite.SQLiteDatabase;

import pl.projektorion.krzysztof.blesensortag.database.tables.interfaces.DBTableInterface;

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

    @Override
    public String getRootReference() {
        return "";
    }

    @Override
    public String getIdReference() {
        return "";
    }
}
