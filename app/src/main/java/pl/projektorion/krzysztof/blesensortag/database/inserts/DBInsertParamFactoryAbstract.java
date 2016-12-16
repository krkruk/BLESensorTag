package pl.projektorion.krzysztof.blesensortag.database.inserts;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;

/**
 * Created by krzysztof on 16.12.16.
 */

public abstract class DBInsertParamFactoryAbstract implements DBInsertParamFactoryInterface {
    private DBRowWriter dbWriter;

    public DBInsertParamFactoryAbstract(DBRowWriter dbWriter) {
        this.dbWriter = dbWriter;
    }

    protected DBRowWriter getDbWriter() { return dbWriter; }
}
