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

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectRootRecordData;
import pl.projektorion.krzysztof.blesensortag.fragments.app.DBRootRecordDisplayFragment;

public class DBSelectRootActivity extends Activity {

    private Fragment fragment;
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DBSelectRootRecordData record = intent.getParcelableExtra(
                    DBRootRecordDisplayFragment.EXTRA_ROOT_RECORD_DATA);


            final Intent sensorListIntent = new Intent(DBSelectRootActivity.this,
                    DBSelectSensorActivity.class);
            sensorListIntent.putExtra(DBSelectSensorActivity.EXTRA_ROOT_RECORD_DATA, record);
            startActivity(sensorListIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbselect_root);

        init_broadcast_receivers();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                new IntentFilter(DBRootRecordDisplayFragment.ACTION_RECORD_SELECTED));
    }

    private void negotiate_db_root_fragment()
    {
        FragmentManager fm = getFragmentManager();

        fragment = new DBRootRecordDisplayFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.dbselect_root_frame, fragment);
        ft.commit();
    }

    private void kill_broadcast_receivers()
    {
        broadcastManager.unregisterReceiver(receiver);
    }
}
