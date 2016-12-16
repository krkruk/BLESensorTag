package pl.projektorion.krzysztof.blesensortag.database.inserts;

import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;

/**
 * Created by krzysztof on 01.12.16.
 */

public abstract class DBInsertAbstract implements DBInsertInterface, Observer {
    private String tableName;
    protected long rootRowId;
    protected DBRowWriter dbWriter;

    public DBInsertAbstract(DBRowWriter dbWriter, long rootRowId, String tableName) {
        this.dbWriter = dbWriter;
        this.rootRowId = rootRowId;
        this.tableName = tableName;
    }

    public DBInsertAbstract(DBRowWriter dbWriter, String tableName) {
        this.dbWriter = dbWriter;
        this.tableName = tableName;
        this.rootRowId = this.dbWriter.getRootRowId();
    }

    @Override
    public String getTableName() {
        return tableName;
    }
}
