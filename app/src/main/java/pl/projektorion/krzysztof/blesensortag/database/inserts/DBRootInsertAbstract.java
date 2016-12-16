package pl.projektorion.krzysztof.blesensortag.database.inserts;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by krzysztof on 01.12.16.
 */

public abstract class DBRootInsertAbstract implements DBRootInsertInterface {
    private SQLiteDatabase db;
    private String tableName;
    private long rootRowId = -1;

    public DBRootInsertAbstract(SQLiteDatabase db, String tableName) {
        this.db = db;
        this.tableName = tableName;
    }

    /**
     * Get root id. If no root row id is specified, the instance
     * creates a new row and read the last id.
     * @return int - Last ID in given root table
     */
    @Override
    public long getRootRowId() {
        if( rootRowId < 0 )
            rootRowId =  create_new_row(db);

        return rootRowId;
    }

    public String getTableName()
    {
        return tableName;
    }

    /**
     * Create a new root row.
     * @param db {@link SQLiteDatabase} link to currently processed SQLiteDB
     * @return ID of a newly created row
     */
    protected abstract long create_new_row(SQLiteDatabase db);

}
