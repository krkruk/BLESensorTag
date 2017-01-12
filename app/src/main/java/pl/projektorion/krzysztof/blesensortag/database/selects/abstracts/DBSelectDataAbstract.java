package pl.projektorion.krzysztof.blesensortag.database.selects.abstracts;

import android.database.Cursor;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * Created by krzysztof on 20.12.16.
 */

public abstract class DBSelectDataAbstract implements DBSelectInterface {

    public DBSelectDataAbstract() {
    }

    public DBSelectDataAbstract(Cursor cursor) {
        parse(cursor);
    }

    protected abstract void parse(Cursor cursor);
}
