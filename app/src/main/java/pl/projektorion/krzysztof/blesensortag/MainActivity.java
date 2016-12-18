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

import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectRootRecord;
import pl.projektorion.krzysztof.blesensortag.database.selects.DBSelectInterface;
import pl.projektorion.krzysztof.blesensortag.fragments.app.BLeDiscoveryFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.app.DBRecordDisplayFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.test.DBTestFragment;

public class MainActivity extends Activity {

    private static final String BLE_DISCOVERY_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.tag.BLE_DISCOVERY_FRAGMENT";

    private Fragment bleDiscovery;
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
        setContentView(R.layout.activity_main);
//        negotiate_fragment();
//        init_temporary_db();
        display_db_data();
        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(receiver,
                new IntentFilter(DBRecordDisplayFragment.ACTION_RECORD_SELECTED));
    }

    @Override
    protected void onDestroy() {
        broadcastManager.unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void negotiate_fragment()
    {
        FragmentManager fm = getFragmentManager();
        bleDiscovery = fm.findFragmentByTag(BLE_DISCOVERY_FRAGMENT_TAG);
        if( bleDiscovery == null )
        {
            bleDiscovery = BLeDiscoveryFragment.newInstance();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(bleDiscovery, BLE_DISCOVERY_FRAGMENT_TAG);
            transaction.replace(R.id.containter_ble_discovery, bleDiscovery);
            transaction.commit();
        }
    }

    private void init_temporary_db()
    {
        FragmentManager fm = getFragmentManager();
        bleDiscovery = fm.findFragmentByTag(BLE_DISCOVERY_FRAGMENT_TAG);
        if( bleDiscovery == null )
        {
            bleDiscovery = new DBTestFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(bleDiscovery, BLE_DISCOVERY_FRAGMENT_TAG);
            transaction.replace(R.id.containter_ble_discovery, bleDiscovery);
            transaction.commit();
        }
    }

    private void display_db_data()
    {
        FragmentManager fm = getFragmentManager();
        bleDiscovery = fm.findFragmentByTag(BLE_DISCOVERY_FRAGMENT_TAG);
        if( bleDiscovery == null )
        {
            bleDiscovery = new DBRecordDisplayFragment();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(bleDiscovery, BLE_DISCOVERY_FRAGMENT_TAG);
            transaction.replace(R.id.containter_ble_discovery, bleDiscovery);
            transaction.commit();
        }
    }
}
