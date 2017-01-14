package pl.projektorion.krzysztof.blesensortag.fragments.app;


import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


import pl.projektorion.krzysztof.blesensortag.R;
import pl.projektorion.krzysztof.blesensortag.adapters.DBRootTableAdapter;
import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.utils.path.PathExternal;
import pl.projektorion.krzysztof.blesensortag.utils.path.PathInterface;
import pl.projektorion.krzysztof.blesensortag.database.tables.DBRootTableRecord;

/**
 * A simple {@link ListFragment} subclass.
 */
public class DBRootRecordDisplayFragment extends ListFragment {

    public static final String ACTION_RECORD_SELECTED =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.action.RECORD_SELECTED";
    public static final String EXTRA_ROOT_RECORD_DATA =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.extra.ROOT_RECORD_DATA";

    private View view;
    private SQLiteDatabase db;
    private Cursor cursor;

    private CursorAdapter cursorAdapter;

    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_broadcast_manager();
        init_db_cursor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init_adapter();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Parcelable record = (Parcelable) cursorAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ROOT_RECORD_DATA, record);
        broadcast_message(ACTION_RECORD_SELECTED, bundle);
    }

    @Override
    public void onDestroy() {
        if( cursor != null ) cursor.close();
        if( db != null ) db.close();
        super.onDestroy();
    }

    private void init_broadcast_manager()
    {
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity().getApplicationContext());
    }

    private void init_db_cursor()
    {
        PathInterface path = new PathExternal(Constant.DB_NAME, Constant.DB_APP_DIR);
        String fullDbPath = path.getFull();
        if( Constant.DB_NAME.equals(fullDbPath) )
            fullDbPath = getActivity().getApplicationContext()
                    .getDatabasePath(Constant.DB_NAME).getAbsolutePath();
        
        try {
            db = SQLiteDatabase.openDatabase(fullDbPath, null, SQLiteDatabase.OPEN_READONLY);
            cursor = db.query(
                    DBRootTableRecord.TABLE_NAME,
                    new String[]{DBRootTableRecord.COLUMN_ID, DBRootTableRecord.COLUMN_DATE},
                    null, null, null, null, DBRootTableRecord.COLUMN_ID + " DESC", null);
        } catch (Exception e) {
            cursor = null;
            Log.e("SQL", "Somethings is wrong: " + e);
            Toast.makeText(getActivity(), R.string.toast_database_empty, Toast.LENGTH_LONG).show();
        }
    }

    private void init_adapter()
    {
        cursorAdapter = new DBRootTableAdapter(getActivity(),
                cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(cursorAdapter);
    }

    private void broadcast_message(String action, Bundle extras)
    {
        final Intent intent = new Intent(action);
        if( extras != null ) intent.putExtras(extras);
        localBroadcastManager.sendBroadcast(intent);
    }
}
