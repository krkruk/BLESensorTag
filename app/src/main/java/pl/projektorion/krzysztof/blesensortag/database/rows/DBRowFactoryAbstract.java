package pl.projektorion.krzysztof.blesensortag.database.rows;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;

/**
 * Created by krzysztof on 02.12.16.
 */

public abstract class DBRowFactoryAbstract implements DBRowFactoryInterface {
    private DBRowWriter dbWriter;
    private String tableName;

    public DBRowFactoryAbstract(DBRowWriter dbWriter, String tableName) {
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
