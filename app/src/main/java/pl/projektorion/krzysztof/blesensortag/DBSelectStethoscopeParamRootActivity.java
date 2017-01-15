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

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectRootRecordData;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope.DBSelectStethoscopeParam;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope.DBSelectStethoscopeParamData;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope.DBSelectStethoscopeParamRoot;
import pl.projektorion.krzysztof.blesensortag.database.selects.sensors.Stethoscope.DBSelectStethoscopeParamRootData;
import pl.projektorion.krzysztof.blesensortag.fragments.app.DBSelectStethoscopeParamRootFragment;

public class DBSelectStethoscopeParamRootActivity extends Activity {

    private static final String STETHOSCOPE_PARAM_ROOT_FRAG_TAG =
        "pl.projektorion.krzysztof.blesensortag.tag.STETHOSCOPE_PARAM_ROOT_FRAG";

    private Fragment fragment;
    private LocalBroadcastManager broadcaster;

    private BroadcastReceiver stethoscopeParamRootReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DBSelectInterface record = intent
                    .getParcelableExtra(DBSelectStethoscopeParamRootFragment.EXTRA_RECORD_DATA);
            final long _id = (long) record
                    .getData(DBSelectStethoscopeParamRootData.ATTRIBUTE_ID);
            final long idRecord = (long) record
                    .getData(DBSelectStethoscopeParamRootData.ATTRIBUTE_ID_RECORD);
            final long dateSeconds = (long) record
                    .getData(DBSelectStethoscopeParamRootData.ATTRIBUTE_DATE_SECONDS);
            final long notifyPeriod = (long) record
                    .getData(DBSelectStethoscopeParamRootData.ATTRIBUTE_NOTIFY_PERIOD);

            final DBSelectInterface rootRecord = new DBSelectRootRecordData(_id, dateSeconds);
            final DBSelectInterface sensorParam = new DBSelectStethoscopeParamData(_id, idRecord, notifyPeriod);
            final DBSelectStethoscopeParam param = new DBSelectStethoscopeParam(rootRecord);
            final String label = param.getLabel();

            final Intent presentStethoscope = new Intent(DBSelectStethoscopeParamRootActivity.this,
                    DBPresentSensorActivity.class);
            presentStethoscope.putExtra(DBPresentSensorActivity.EXTRA_ROOT_RECORD, rootRecord);
            presentStethoscope.putExtra(DBPresentSensorActivity.EXTRA_SENSOR_RECORD, sensorParam);
            presentStethoscope.putExtra(DBPresentSensorActivity.EXTRA_SENSOR_LABEL, label);
            startActivity(presentStethoscope);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbselect_stethoscope_param_root);
        init_broadcast_receivers();
        negotiate_fragment();
    }

    @Override
    protected void onDestroy() {
        kill_broadcast_receivers();
        super.onDestroy();
    }

    private void init_broadcast_receivers()
    {
        broadcaster = LocalBroadcastManager.getInstance(getApplicationContext());
        broadcaster.registerReceiver(stethoscopeParamRootReceiver,
                new IntentFilter(DBSelectStethoscopeParamRootFragment.ACTION_RECORD_SELECTED));
    }

    private void negotiate_fragment()
    {
        FragmentManager fm = getFragmentManager();
        fragment = fm.findFragmentByTag(STETHOSCOPE_PARAM_ROOT_FRAG_TAG);
        if( fragment == null )
        {
            fragment = new DBSelectStethoscopeParamRootFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(fragment, STETHOSCOPE_PARAM_ROOT_FRAG_TAG);
            ft.replace(R.id.stethoscope_param_root_frag_container, fragment);
            ft.commit();
        }
    }

    private void kill_broadcast_receivers()
    {
        broadcaster.unregisterReceiver(stethoscopeParamRootReceiver);
    }
}
