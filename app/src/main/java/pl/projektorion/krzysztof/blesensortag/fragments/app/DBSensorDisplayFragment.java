package pl.projektorion.krzysztof.blesensortag.fragments.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.adapters.DBSelectSensorAdapter;
import pl.projektorion.krzysztof.blesensortag.database.DBHelper;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQuery;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryExecutor;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryInterface;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBQueryListenerInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectGeneralSensorParamData;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.factories.DBFactoryParamSelects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBSensorDisplayFragment extends Fragment {

    public static final String EXTRA_ROOT_RECORD_DATA =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.extra.ROOT_RECORD_DATA";

    private View view;
    private Context context;
    private Activity embeddedActivity;
    private DBSelectSensorAdapter availableSensorAdapter;

    private DBSelectInterface rootRecord;

    private Class intentClass;
    private String intentExtraRootRecord;
    private String intentExtraSensorRecord;
    private String intentExtraSensorLabel;

    private AdapterView.OnItemClickListener tableSelected = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DBQueryListenerInterface sensorTable = (DBQueryListenerInterface)
                    availableSensorAdapter.getItem(position);

            if( intentClass == null
                    || intentExtraRootRecord == null
                    || intentExtraSensorRecord == null
                    || intentExtraSensorLabel == null) {
                Log.d("DBSenDispFrag", "No params to start an activity");
                return;
            }

            final Intent intent = new Intent(context, intentClass);
            intent.putExtra(intentExtraRootRecord, rootRecord);
            intent.putExtra(intentExtraSensorRecord, sensorTable.getRecord());
            intent.putExtra(intentExtraSensorLabel, sensorTable.getLabel());
            embeddedActivity.startActivity(intent);
        }
    };

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
        context = getActivity().getApplicationContext();
        embeddedActivity = getActivity();
        acquire_data();
        verify_record_existence();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dbsensor_display, container, false);
        init_widgets();
        return view;
    }

    public void setIntentClass(Class intentClass) {
        this.intentClass = intentClass;
    }

    public void setIntentExtraSensorRecord(String intentExtraSensorRecord) {
        this.intentExtraSensorRecord = intentExtraSensorRecord;
    }

    public void setIntentExtraRootRecord(String intentExtraRootRecord) {
        this.intentExtraRootRecord = intentExtraRootRecord;
    }

    public void setIntentExtraSensorLabel(String intentExtraSensorLabel) {
        this.intentExtraSensorLabel = intentExtraSensorLabel;
    }

    private void acquire_data() throws NullPointerException
    {
        final Bundle bundle = getArguments();
        rootRecord = bundle.getParcelable(EXTRA_ROOT_RECORD_DATA);
        if( rootRecord == null )
            throw new NullPointerException("Cannot pass null value of the root record");
    }

    private void verify_record_existence()
    {
        AsyncTask task = new SQLRead();
        task.execute(new Void[]{});
    }

    private void init_widgets()
    {
        ListView availableSensorRecords = (ListView) view.findViewById(R.id.available_sensor_records);
        availableSensorAdapter = new DBSelectSensorAdapter(context);

        availableSensorRecords.setAdapter(availableSensorAdapter);
        availableSensorRecords.setOnItemClickListener(tableSelected);
    }

    private class SQLRead extends AsyncTask<Void, Void, Void> {
        private DBHelper helper;
        private Context context;
        private List<DBQueryListenerInterface> listeners;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context = DBSensorDisplayFragment.this.getActivity().getApplicationContext();

            DBFactoryParamSelects factoryParamSelects = new DBFactoryParamSelects(rootRecord);
            listeners = factoryParamSelects.getQueryListeners();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            availableSensorAdapter.clear();
            availableSensorAdapter.setData(listeners);
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

            filter_listeners();
            return null;
        }

        private List<DBQueryInterface> prepare_queries(SQLiteDatabase dbReadable)
        {
            List<DBQueryInterface> queries = new ArrayList<>();

            for(DBQueryListenerInterface listener : listeners)
                queries.add(new DBQuery(dbReadable, listener));

            return queries;
        }

        private void filter_listeners() {
            final long NON_EXISTENT_ID = 0;

            for (int i = 0; i < listeners.size(); i++) {
                DBSelectInterface record = listeners.get(i).getRecord();
                if ((long) record.getData(DBSelectInterface.ATTRIBUTE_ID) <= NON_EXISTENT_ID)
                    listeners.remove(i--);
            }
        }
    }
}
