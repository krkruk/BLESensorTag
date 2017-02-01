package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.HeartRate;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.bluetooth.GeneralProfile.HeartRate.HeartRateData;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriteCommand;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.HeartRate.DBTableHeartRate;

/**
 * Created by krzysztof on 01.02.17.
 */

public class DBInsertHeartRate extends DBInsertAbstract {
    protected DBInsertHeartRate(DBRowWriter dbWriter, long rootRowId) {
        super(dbWriter, rootRowId, DBTableHeartRate.TABLE_NAME);
    }

    protected DBInsertHeartRate(DBRowWriter dbWriter) {
        super(dbWriter, DBTableHeartRate.TABLE_NAME);
    }

    @Override
    public void update(Observable o, Object arg) {
        HeartRateData heartRateData = (HeartRateData) arg;
        final double heartRate = heartRateData.getValue(HeartRateData.ATTRIBUTE_HEART_RATE);

        ContentValues values = new ContentValues();
        values.put(DBTableHeartRate.COLUMN_ROOT_REF, rootRowId);
        values.put(DBTableHeartRate.COLUMN_HEART_RATE, heartRate);

        final SQLiteDatabase db = dbWriter.getWritableDatabase();
        dbWriter.add(new DBRowWriteCommand(db, DBTableHeartRate.TABLE_NAME, values));
    }
}
