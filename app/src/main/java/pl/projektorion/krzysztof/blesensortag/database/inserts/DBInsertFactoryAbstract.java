package pl.projektorion.krzysztof.blesensortag.database.inserts;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;

/**
 * Created by krzysztof on 02.12.16.
 */

public abstract class DBInsertFactoryAbstract implements DBInsertFactoryInterface {
    private DBRowWriter dbWriter;

    public DBInsertFactoryAbstract(DBRowWriter dbWriter) {
        this.dbWriter = dbWriter;
    }

    protected DBRowWriter getDbWriter()
    {
        return dbWriter;
    }
}
