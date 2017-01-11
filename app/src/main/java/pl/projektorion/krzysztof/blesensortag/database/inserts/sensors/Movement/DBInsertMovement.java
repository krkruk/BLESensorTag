package pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Movement;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Observable;

import pl.projektorion.krzysztof.blesensortag.bluetooth.SensorTag.Movement.MovementData;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.ProfileData;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriteCommand;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.abstracts.DBInsertAbstract;
import pl.projektorion.krzysztof.blesensortag.database.tables.sensors.Movement.DBTableMovement;

/**
 * Created by krzysztof on 01.12.16.
 */

public class DBInsertMovement extends DBInsertAbstract {
    private Observable observable;

    public DBInsertMovement(DBRowWriter dbWriter, long rootRowId) {
        super(dbWriter, rootRowId, DBTableMovement.TABLE_NAME);
    }

    public DBInsertMovement(DBRowWriter dbWriter) {
        super(dbWriter, DBTableMovement.TABLE_NAME);
    }

    @Override
    public void update(Observable o, Object arg) {
        observable = o;
        final ProfileData data = (ProfileData) arg;
        final double accX = data.getValue(MovementData.ATTRIBUTE_ACC_X);
        final double accY = data.getValue(MovementData.ATTRIBUTE_ACC_Y);
        final double accZ = data.getValue(MovementData.ATTRIBUTE_ACC_Z);
        final double gyroX = data.getValue(MovementData.ATTRIBUTE_GYRO_X);
        final double gyroY = data.getValue(MovementData.ATTRIBUTE_GYRO_Y);
        final double gyroZ = data.getValue(MovementData.ATTRIBUTE_GYRO_Z);
        final double magnetX = data.getValue(MovementData.ATTRIBUTE_MAGNET_X);
        final double magnetY = data.getValue(MovementData.ATTRIBUTE_MAGNET_Y);
        final double magnetZ = data.getValue(MovementData.ATTRIBUTE_MAGNET_Z);

        ContentValues dbValues = new ContentValues();
        dbValues.put(DBTableMovement.COLUMN_ROOT_REF, rootRowId);
        dbValues.put(DBTableMovement.COLUMN_ACC_X, accX);
        dbValues.put(DBTableMovement.COLUMN_ACC_Y, accY);
        dbValues.put(DBTableMovement.COLUMN_ACC_Z, accZ);
        dbValues.put(DBTableMovement.COLUMN_GYR_X, gyroX);
        dbValues.put(DBTableMovement.COLUMN_GYR_Y, gyroY);
        dbValues.put(DBTableMovement.COLUMN_GYR_Z, gyroZ);
        dbValues.put(DBTableMovement.COLUMN_MAG_X, magnetX);
        dbValues.put(DBTableMovement.COLUMN_MAG_Y, magnetY);
        dbValues.put(DBTableMovement.COLUMN_MAG_Z, magnetZ);

        final SQLiteDatabase db = dbWriter.getWritableDatabase();
        dbWriter.add(new DBRowWriteCommand(db, getTableName(), dbValues));
    }
}
