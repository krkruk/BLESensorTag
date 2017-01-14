package pl.projektorion.krzysztof.blesensortag.database;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.os.ResultReceiver;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryListenerInterface;
import pl.projektorion.krzysztof.blesensortag.utils.path.PathExternal;
import pl.projektorion.krzysztof.blesensortag.utils.path.PathInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class DBSelectIntentService extends IntentService {

    public static final String EXTRA_SENSOR_DATA_SELECT =
            "pl.projektorion.krzysztof.blesensortag.database.extra.SENSOR_DATA_SELECT";

    public static final String EXTRA_RESULT_RECEIVER =
            "pl.projektorion.krzysztof.blesensortag.database.extra.RESULT_RECEIVER";

    public static final String EXTRA_RESULT =
            "pl.projektorion.krzysztof.blesensortag.database.extra.RESULT";

    public static final int EXTRA_RESULT_CODE = 111;

    private DBQueryListenerInterface sensorData;
    private ResultReceiver receiver;

    public DBSelectIntentService() {
        super("DBSelectIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorData = intent.getParcelableExtra(EXTRA_SENSOR_DATA_SELECT);
        receiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Context context = getApplicationContext();
        PathInterface dbPath = new PathExternal(Constant.DB_NAME, Constant.DB_APP_DIR);

        SQLiteOpenHelper helper = new DBHelper(context, dbPath, null);
        SQLiteDatabase dbReadable = helper.getReadableDatabase();

        Cursor cursor = null;
        try {
            dbReadable.beginTransaction();
            cursor = dbReadable.rawQuery(sensorData.getQuery(), sensorData.getQueryData());
            if (!cursor.moveToFirst())
                return;
            sensorData.onQueryExecuted(cursor);
            dbReadable.setTransactionSuccessful();
        }finally {
            dbReadable.endTransaction();
            if( cursor != null )
                cursor.close();
            dbReadable.close();
        }

        List<? extends DBSelectInterface> data = sensorData.getRecords();
        ArrayList<? extends Parcelable> d = new ArrayList<>(data);

        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_RESULT, d);
        receiver.send(EXTRA_RESULT_CODE, bundle);
    }
}
