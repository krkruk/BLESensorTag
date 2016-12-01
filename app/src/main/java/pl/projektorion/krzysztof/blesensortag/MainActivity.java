package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import pl.projektorion.krzysztof.blesensortag.constants.Constant;
import pl.projektorion.krzysztof.blesensortag.database.DBService;
import pl.projektorion.krzysztof.blesensortag.fragments.app.BLeDiscoveryFragment;

public class MainActivity extends Activity {

    private static final String BLE_DISCOVERY_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.tag.BLE_DISCOVERY_FRAGMENT";

    private Fragment bleDiscovery;

    private DBService dbService;
    private ServiceConnection dbServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("Activity", "DB Service Bound");
            dbService = ((DBService.DBServiceBinder) service).getService();
            dbService.initDatabase();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        negotiate_fragment();
        init_temporary_db();
    }

    @Override
    protected void onDestroy() {
        clean_up();
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
        deleteDatabase(Constant.DB_NAME);
        bindService(new Intent(this, DBService.class), dbServiceConn, BIND_AUTO_CREATE);
    }

    private void clean_up()
    {
        unbindService(dbServiceConn);
    }
}
