package pl.projektorion.krzysztof.blesensortag.database.tables.interfaces;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by krzysztof on 01.12.16.
 */

public interface DBTableInterface {

    void createTable(SQLiteDatabase db);

    String getTableName();

    String getRootReference();

    String getIdReference();
}
