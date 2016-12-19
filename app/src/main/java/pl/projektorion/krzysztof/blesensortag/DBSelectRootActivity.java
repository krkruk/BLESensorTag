package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectRootRecord;
import pl.projektorion.krzysztof.blesensortag.fragments.app.DBRecordDisplayFragment;

public class DBSelectRootActivity extends Activity {

    private static final String DB_ROOT_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.tag.DB_ROOT_FRAGMENT";

    private Fragment fragment;
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DBSelectRootRecord record = intent.getParcelableExtra(
                    DBRecordDisplayFragment.EXTRA_ROOT_RECORD_DATA);
            final long _id = (long) record.getData(DBSelectInterface.ATTRIBUTE_ID);
            final long date = (long) record.getData(DBSelectRootRecord.ATTRIBUTE_DATE_SECONDS);
            Log.i("CLICKED", String.format("ID: %d, created: %d", _id, date));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbselect_root);

        init_broadcast_receivers();
        negotiate_db_root_fragment();
    }

    @Override
    protected void onDestroy() {
        kill_broadcast_receivers();
        super.onDestroy();
    }

    private void init_broadcast_receivers()
    {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(receiver,
                new IntentFilter(DBRecordDisplayFragment.ACTION_RECORD_SELECTED));
    }

    private void negotiate_db_root_fragment()
    {
        FragmentManager fm = getFragmentManager();
        fragment = fm.findFragmentByTag(DB_ROOT_FRAGMENT_TAG);
        if( fragment == null )
        {
            fragment = new DBRecordDisplayFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(fragment, DB_ROOT_FRAGMENT_TAG);
            ft.replace(R.id.dbselect_root_frame, fragment);
            ft.commit();
        }
    }

    private void kill_broadcast_receivers()
    {
        broadcastManager.unregisterReceiver(receiver);
    }
}
