package pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertParamFactoryInterface;

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
