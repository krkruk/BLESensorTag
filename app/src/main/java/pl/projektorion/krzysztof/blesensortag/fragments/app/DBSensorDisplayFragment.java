package pl.projektorion.krzysztof.blesensortag.fragments.app;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.DBHelper;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQuery;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryExecutor;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectBarometerParam;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectBarometerParamData;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectRootRecordData;
import pl.projektorion.krzysztof.blesensortag.factories.DBFactoryTables;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBSensorDisplayFragment extends Fragment {

    public static final String EXTRA_ROOT_RECORD_DATA =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.extra.ROOT_RECORD_DATA";

    private DBSelectInterface rootRecord;
    private DBFactoryTables tableFactory = new DBFactoryTables();


    public DBSensorDisplayFragment() {
    }

    public static DBSensorDisplayFragment newInstance(Parcelable selectedRootRecord) {

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ROOT_RECORD_DATA, selectedRootRecord);
        DBSensorDisplayFragment fragment = new DBSensorDisplayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acquire_data();
        AsyncTask task = new SQLRead();
        task.execute(new Void[]{});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dbsensor_display, container, false);
    }

    private void acquire_data() throws NullPointerException
    {
        final Bundle bundle = getArguments();
        rootRecord = bundle.getParcelable(EXTRA_ROOT_RECORD_DATA);
        if( rootRecord == null ) {
            throw new NullPointerException("Cannot pass null value of the root record");
        }
        final long _id = (long) rootRecord.getData(DBSelectInterface.ATTRIBUTE_ID);
        final long date = (long) rootRecord.getData(DBSelectRootRecordData.ATTRIBUTE_DATE_SECONDS);
        Log.i("CLICKED", String.format("ID: %d, created: %d", _id, date));
    }

    private class SQLRead extends AsyncTask<Void, Void, Void> {
        private DBHelper helper;
        private Context context;
        private DBSelectBarometerParam barometerParam;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context = DBSensorDisplayFragment.this.getActivity().getApplicationContext();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            helper = new DBHelper( context, null );
            SQLiteDatabase db = helper.getReadableDatabase();

            DBQueryExecutor executor = new DBQueryExecutor(prepare_queries(db));
            db.beginTransaction();
            try {
                executor.executeAll();
                db.setTransactionSuccessful();
            }
            finally {
                db.endTransaction();
                db.close();
            }

            return null;
        }

        private List<DBQueryInterface> prepare_queries(SQLiteDatabase dbReadable)
        {
            List<DBQueryInterface> queries = new ArrayList<>();
            barometerParam = new DBSelectBarometerParam(rootRecord);
            queries.add(new DBQuery(dbReadable, barometerParam));
            return queries;
        }
    }
}
