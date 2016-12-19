package pl.projektorion.krzysztof.blesensortag.fragments.app;


import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.ContentValues;
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

import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.database.DBHelper;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectRootRecord;

/**
 * A simple {@link Fragment} subclass.
 */
public class DBSensorDisplayFragment extends Fragment {

    public static final String EXTRA_ROOT_RECORD_DATA =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.extra.ROOT_RECORD_DATA";

    private DBSelectInterface rootRecord;

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
        final long date = (long) rootRecord.getData(DBSelectRootRecord.ATTRIBUTE_DATE_SECONDS);
        Log.i("CLICKED", String.format("ID: %d, created: %d", _id, date));
    }

    private class SQLRead extends AsyncTask<Void, Void, Void> {
        private DBHelper helper;
        private boolean barometerParamExists = false;
        private Context context;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            context = DBSensorDisplayFragment.this.getActivity().getApplicationContext();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "BarometerParam state: " + Boolean.toString(barometerParamExists),
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            helper = new DBHelper( context, null );
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from BarometerParam where ID_RECORD = ? limit 1",
                    new String[]{Long.toString((long) rootRecord.getData(DBSelectRootRecord.ATTRIBUTE_ID))});
            if(cursor.moveToFirst()) barometerParamExists = true;
            cursor.close();
            db.close();
            return null;
        }
    }
}
