package pl.projektorion.krzysztof.blesensortag.database.inserts;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBRootInsertAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBRootTableRecord;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBInsertRootRecord extends DBRootInsertAbstract {

    public static final String COLUMN_DATE = DBRootTableRecord.COLUMN_DATE;
    public static final String COLUMN_ID = DBRootTableRecord.COLUMN_ID;

    public DBInsertRootRecord(SQLiteDatabase db, String tableName) {
        super(db, tableName);
    }

    @Override
    protected long create_new_row(SQLiteDatabase db) {
        ContentValues newRecord = new ContentValues();
        newRecord.put(COLUMN_DATE, System.currentTimeMillis() / 1000L);
        return db.insert(
                getTableName(),
                null,
                newRecord
        );
    }
}
