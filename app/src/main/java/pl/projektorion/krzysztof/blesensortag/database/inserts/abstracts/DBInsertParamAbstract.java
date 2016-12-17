package pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts;

import android.content.ContentValues;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriteCommand;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBParamDataInterface;
import pl.projektorion.krzysztof.blesensortag.database.inserts.interfaces.DBInsertParamInterface;

/**
 * Created by krzysztof on 16.12.16.
 */

public abstract class DBInsertParamAbstract implements DBInsertParamInterface {
    protected DBRowWriter dbWriter;
    private String tableName;

    private DBInsertParamAbstract() {}

    protected DBInsertParamAbstract(DBRowWriter dbWriter, String tableName) {
        this.dbWriter = dbWriter;
        this.tableName = tableName;
    }

    @Override
    public void insert(DBParamDataInterface data) {
        dbWriter.add(new DBRowWriteCommand(
                dbWriter.getWritableDatabase(),
                getTableName(),
                values(data)
        ));
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    protected abstract ContentValues values(DBParamDataInterface data);
}
