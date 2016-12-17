package pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts;

import java.util.Observer;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertInterface;

/**
 * Created by krzysztof on 01.12.16.
 */

public abstract class DBInsertAbstract implements DBInsertInterface, Observer {
    private String tableName;
    protected long rootRowId;
    protected DBRowWriter dbWriter;

    protected DBInsertAbstract(DBRowWriter dbWriter, long rootRowId, String tableName) {
        this.dbWriter = dbWriter;
        this.rootRowId = rootRowId;
        this.tableName = tableName;
    }

    protected DBInsertAbstract(DBRowWriter dbWriter, String tableName) {
        this.dbWriter = dbWriter;
        this.tableName = tableName;
        this.rootRowId = this.dbWriter.getRootRowId();
    }

    @Override
    public String getTableName() {
        return tableName;
    }
}
