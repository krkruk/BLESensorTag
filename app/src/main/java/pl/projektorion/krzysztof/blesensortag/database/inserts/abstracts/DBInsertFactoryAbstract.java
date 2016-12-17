package pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertFactoryInterface;

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
