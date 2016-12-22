package pl.projektorion.krzysztof.blesensortag.database;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import pl.projektorion.krzysztof.blesensortag.database.commands.DBQuery;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometer;
import pl.projektorion.krzysztof.blesensortag.database.selects.Barometer.DBSelectBarometerData;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectGeneralSensorParamData;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class DBSelectIntentService extends IntentService {

    public static final String EXTRA_SENSOR_DATA_SELECT =
            "pl.projektorion.krzysztof.blesensortag.database.extra.SENSOR_DATA_SELECT";

    private DBQueryListenerInterface sensorData;

    public DBSelectIntentService() {
        super("DBSelectIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorData = intent.getParcelableExtra(EXTRA_SENSOR_DATA_SELECT);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Context context = getApplicationContext();

        SQLiteOpenHelper helper = new DBHelper(context, null);

        SQLiteDatabase dbReadable = helper.getReadableDatabase();

        Cursor cursor = dbReadable.rawQuery(sensorData.getQuery(), sensorData.getQueryData());
        if( !cursor.moveToFirst() )
            return;
        sensorData.onQueryExecuted(cursor);

        List<? extends DBSelectInterface> data = sensorData.getRecords();

        Log.i("Header", sensorData.getLabel());
        for(DBSelectInterface echo : data)
            Log.i("DATA", echo.toString());
    }
}
