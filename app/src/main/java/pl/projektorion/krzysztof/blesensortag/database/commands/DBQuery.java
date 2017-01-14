package pl.projektorion.krzysztof.blesensortag.database.commands;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by krzysztof on 20.12.16.
 */

public class DBQuery implements DBQueryInterface {

    private SQLiteDatabase dbReadable;
    private DBQueryListenerInterface queryListener;

    public DBQuery(SQLiteDatabase dbReadable, DBQueryListenerInterface queryListener) {
        this.dbReadable = dbReadable;
        this.queryListener = queryListener;
    }

    @Override
    public void execute() {
        Cursor cursor = dbReadable.rawQuery(
                queryListener.getQuery(),
                queryListener.getQueryData());
        Log.e("Query", queryListener.getLabel() + "  " + queryListener.getQuery());
        if( cursor.moveToFirst() ) queryListener.onQueryExecuted(cursor);

        cursor.close();
    }
}
