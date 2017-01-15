package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Stethoscope;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.bluetooth.CustomProfile.StethoscopeData;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriteCommand;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Stethoscope.DBTableStethoscope;

/**
 * Created by krzysztof on 14.01.17.
 */

public class DBInsertStethoscope extends DBInsertAbstract {

    private Observable observable;

    protected DBInsertStethoscope(DBRowWriter dbWriter, long rootRowId) {
        super(dbWriter, rootRowId, DBTableStethoscope.TABLE_NAME);
    }

    protected DBInsertStethoscope(DBRowWriter dbWriter) {
        super(dbWriter, DBTableStethoscope.TABLE_NAME);
    }

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        if( !(arg instanceof StethoscopeData) ) return;
        StethoscopeData data = (StethoscopeData) arg;
        final double first = data.getValue(StethoscopeData.ATTRIBUTE_FIRST);

        final ContentValues values = new ContentValues();
        values.put(DBTableStethoscope.COLUMN_ROOT_REF, rootRowId);
        values.put(DBTableStethoscope.COLUMN_DATA, first);

        final SQLiteDatabase db = dbWriter.getWritableDatabase();
        dbWriter.add(new DBRowWriteCommand(db, getTableName(), values));
    }

    @Override
    public String toString() {
        return "DBInsertStethoscope";
    }
}
