package pl.projektorion.krzysztof.blesensortag.database.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by krzysztof on 01.12.16.
 */

public interface DBTableInterface {

    void createTable(SQLiteDatabase db);

    String getTableName();
}
