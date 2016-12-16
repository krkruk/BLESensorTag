package pl.projektorion.krzysztof.blesensortag.database.inserts;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;

/**
 * Created by krzysztof on 02.12.16.
 */

public abstract class DBInsertFactoryAbstract implements DBInsertFactoryInterface {
    private DBRowWriter dbWriter;
    private String tableName;

    public DBInsertFactoryAbstract(DBRowWriter dbWriter, String tableName) {
        this.dbWriter = dbWriter;
        this.tableName = tableName;
    }

    protected DBRowWriter getDbWriter()
    {
        return dbWriter;
    }

    protected String getTableName()
    {
        return tableName;
    }
}
