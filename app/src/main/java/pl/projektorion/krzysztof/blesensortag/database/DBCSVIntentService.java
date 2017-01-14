package pl.projektorion.krzysztof.blesensortag.database;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryParcelableListenerInterface;
import pl.projektorion.krzysztof.blesensortag.utils.path.PathExternal;
import pl.projektorion.krzysztof.blesensortag.utils.path.PathInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;


/**
 */
public class DBCSVIntentService extends IntentService {

    public static final String EXTRA_QUERY =
            "pl.projektorion.krzysztof.blesensortag.database.extra.QUERY";

    public static final String EXTRA_SENSOR_NAME =
            "pl.projektorion.krzysztof.blesensortag.database.extra.SENSOR_NAME";

    public static final String EXTRA_DATE_SECONDS =
            "pl.projektorion.krzysztof.blesensortag.database.extra.DATE_SECONDS";

    private Context context;
    private Handler handler;
    private DBQueryParcelableListenerInterface query;
    private String sensorName;
    private long date;

    private PathInterface csvFile;
    private static final String ENDLINE = System.getProperty("line.separator");

    public DBCSVIntentService() {
        super("DBCSVIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        handler = new Handler();
        query = intent.getParcelableExtra(EXTRA_QUERY);
        sensorName = intent.getStringExtra(EXTRA_SENSOR_NAME);
        date = intent.getLongExtra(EXTRA_DATE_SECONDS, 0)*1000;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        create_filename();
        load_from_database();
        export_to_csv();
        verify_file_exists();
    }

    private void create_filename()
    {
        final SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd_HH-mm-ss_",
                Locale.getDefault());
        String fileName = formatter.format(new Date(date)) + sensorName + ".csv";
        csvFile = new PathExternal(fileName, Constant.DB_APP_DIR);
        Log.i("EXPORT", "Filename: " + csvFile.getFull());
    }

    private void load_from_database()
    {
        final Context context = getApplicationContext();
        final PathInterface dbPath = new PathExternal(Constant.DB_NAME, Constant.DB_APP_DIR);

        SQLiteOpenHelper helper = new DBHelper(context, dbPath, null);
        SQLiteDatabase dbReadable = helper.getReadableDatabase();

        Cursor cursor = null;
        try {
            dbReadable.beginTransaction();
            cursor = dbReadable.rawQuery(query.getQuery(), query.getQueryData());
            if (!cursor.moveToFirst())
                return;
            query.onQueryExecuted(cursor);
            dbReadable.setTransactionSuccessful();
        }finally {
            dbReadable.endTransaction();
            if( cursor != null )
                cursor.close();
            dbReadable.close();
        }
    }

    private void export_to_csv()
    {
        List<? extends DBSelectInterface> data = query.getRecords();

        DBSelectInterface record;

        try { record = data.remove(0); }
        catch (UnsupportedOperationException | IndexOutOfBoundsException e)
        { post_toast(R.string.toast_export_csv_could_not_read); return; }

        try {
            final FileOutputStream fileOutputStream =
                    new FileOutputStream(new File(csvFile.getFull()), true);
            final OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(
                    record.getData(DBSelectInterface.ATTRIBUTE_CSV_HEADER) + ENDLINE);
            outputStreamWriter.write(record.toString() + ENDLINE);

            while (!data.isEmpty())
            {
                try { record = data.remove(0); }
                catch (UnsupportedOperationException | IndexOutOfBoundsException e)
                { post_toast(R.string.toast_export_csv_could_not_read); break; }

                outputStreamWriter.write(record.toString() + ENDLINE);
            }
            outputStreamWriter.close();
            fileOutputStream.close();
        }
        catch (IOException e) {
            post_toast(R.string.toast_export_csv_could_not_save);
        }
    }

    private void verify_file_exists()
    {
        final File file = new File(csvFile.getFull());
        final String successfulLabel =
                getString(R.string.toast_export_csv_successful) + " " + csvFile.getName();

        if( file.exists() ) post_toast(successfulLabel);
        else post_toast(R.string.toast_export_csv_unknown_error);
    }

    private void post_toast(final int resourceId)
    {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, resourceId, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void post_toast(final String label)
    {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, label, Toast.LENGTH_LONG).show();
            }
        });
    }
}
