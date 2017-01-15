package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import pl.projektorion.krzysztof.blesensortag.bluetooth.CustomProfile.StethoscopeProfile;
import pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattModelService;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattModels;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattProfiles;
import pl.projektorion.krzysztof.blesensortag.database.commands.DBRowWriter;
import pl.projektorion.krzysztof.blesensortag.database.inserts.DBInsertFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Stethoscope.DBInsertStethoscopeFactory;
import pl.projektorion.krzysztof.blesensortag.database.inserts.sensors.Stethoscope.DBInsertStethoscopeParamFactory;
import pl.projektorion.krzysztof.blesensortag.factories.DBFactoryForInsertsFactory;
import pl.projektorion.krzysztof.blesensortag.fragments.app.BLeConfigStethoscopeFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.CustomProfile.StethoscopeFragment;

public class BLePresentStethoscopeActivity extends Activity {

    public static final String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.extra.stethoscope.BLE_DEVICE";

    private static final String STETHOSCOPE_PRESENT_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.extra.stethoscope.FRAGMENT_PRESENT_TAG";

    private static final String STETHOSCOPE_CONFIG_FRAGMENT_TAG =
            "pl.projektorion.krzysztof.blesensortag.extra.stethoscope.FRAGMENT_CONFIG_TAG";

    private static final String EXTRA_RECORD_BUTTON_STATE =
            "pl.projektorion.krzysztof.blesensortag.extra.stethoscope.RECORD_BUTTON_STATE";

    private static final String EXTRA_SERVICE_BIND_STATE =
            "pl.projektorion.krzysztof.blesensortag.extra.stethoscope.SERVICE_BIND_STATE";

    private BluetoothDevice bleDevice;

    private StethoscopeFragment presentationFragment;
    private BLeConfigStethoscopeFragment configFragment;
    private MenuItem recordAction;
    private boolean wasServiceDiscovered = false;

    private LocalBroadcastManager broadcaster;
    private DBServiceBLeService dbService;
    private boolean isServiceBound = false;

    private BroadcastReceiver bleServiceDiscoveredReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            wasServiceDiscovered = true;
            if( recordAction != null ) recordAction.setEnabled(true);
        }
    };

    private ServiceConnection dbServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dbService = ((DBServiceBLeService.DBServiceBLeBinder) service).getService();
            isServiceBound = true;
            setup_database();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_present_stethoscope);
        retrieve_data();
        restore_saved_instance(savedInstanceState);
        negotiate_fragment();
        init_broadcast_receivers();
        init_bound_services();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ble_present_stethoscope, menu);
        init_menu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_record:
                Log.i("RECORD", "Start recording");
                trigger_db_recording();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_RECORD_BUTTON_STATE, wasServiceDiscovered);
        outState.putBoolean(EXTRA_SERVICE_BIND_STATE, isServiceBound);
    }

    @Override
    protected void onDestroy() {
        kill_bound_services();
        kill_broadcast_receivers();
        super.onDestroy();
    }

    private void retrieve_data()
    {
        final Intent intent = getIntent();
        bleDevice = intent.getParcelableExtra(EXTRA_BLE_DEVICE);
    }

    private void restore_saved_instance(Bundle savedInstanceState)
    {
        if( savedInstanceState == null ) return;
        wasServiceDiscovered = savedInstanceState.getBoolean(EXTRA_RECORD_BUTTON_STATE);
        isServiceBound = savedInstanceState.getBoolean(EXTRA_SERVICE_BIND_STATE);
    }

    private void negotiate_fragment()
    {
        final FragmentManager fm = getFragmentManager();
        presentationFragment = (StethoscopeFragment)
                fm.findFragmentByTag(STETHOSCOPE_PRESENT_FRAGMENT_TAG);
        configFragment = (BLeConfigStethoscopeFragment)
                fm.findFragmentByTag(STETHOSCOPE_CONFIG_FRAGMENT_TAG);

        if( presentationFragment == null )
        {
            presentationFragment = new StethoscopeFragment();
            final FragmentTransaction ft = fm.beginTransaction();
            ft.add(presentationFragment, STETHOSCOPE_PRESENT_FRAGMENT_TAG);
            ft.replace(R.id.present_stethoscope_frag_container, presentationFragment);
            ft.commit();
        }
        if( configFragment == null )
        {
            configFragment = BLeConfigStethoscopeFragment.newInstance(bleDevice);
            final FragmentTransaction ft = fm.beginTransaction();
            ft.add(configFragment, STETHOSCOPE_CONFIG_FRAGMENT_TAG);
            ft.replace(R.id.config_stethoscope_frag_container, configFragment);
            ft.commit();
        }
    }

    private void init_broadcast_receivers()
    {
        broadcaster = LocalBroadcastManager.getInstance(getApplicationContext());

        broadcaster.registerReceiver(bleServiceDiscoveredReceiver,
                new IntentFilter(BLeGattModelService.ACTION_GATT_MODELS_CREATED));
    }

    private void init_bound_services()
    {
        final Context ctx = getApplicationContext();
        ctx.bindService(new Intent(ctx, DBServiceBLeService.class),
                dbServiceConn, Context.BIND_AUTO_CREATE);
    }

    private void init_menu(Menu menu)
    {
        recordAction = menu.findItem(R.id.action_record);

        recordAction.setEnabled(wasServiceDiscovered);
    }

    private void trigger_db_recording()
    {
        final Context ctx = getApplicationContext();

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startService(new Intent(ctx, DBServiceBLeService.class));
                ctx.unbindService(dbServiceConn);
                isServiceBound = false;
                moveTaskToBack(true);
            }
        }, 500);
    }

    private void setup_database()
    {
        final BLeAvailableGattModels models = presentationFragment.getModels();
        final BLeAvailableGattProfiles profiles = configFragment.getProfiles();
        final DBFactoryForInsertsFactory insertsFactory = new DBFactoryForInsertsFactory() {
            @Override
            protected DBInsertFactory init_factory(DBInsertFactory factory, DBRowWriter dbRowWriter) {
                factory.put(StethoscopeProfile.STETHOSCOPE_DATA,
                        new DBInsertStethoscopeFactory(dbRowWriter));
                return factory;
            }
        };

        final DBFactoryForInsertsFactory insertParamsFactory = new DBFactoryForInsertsFactory() {
            @Override
            protected DBInsertFactory init_factory(DBInsertFactory factory, DBRowWriter dbRowWriter) {
                factory.put(StethoscopeProfile.STETHOSCOPE_SERVICE,
                        new DBInsertStethoscopeParamFactory(dbRowWriter));
                return factory;
            }
        };

        dbService.setModels(models);
        dbService.setProfiles(profiles);
        dbService.setInsertsFactory(insertsFactory);
        dbService.setInsertParamsFactory(insertParamsFactory);
    }

    private void kill_bound_services()
    {
        final Context ctx = getApplicationContext();
        if( isServiceBound ) ctx.unbindService(dbServiceConn);
    }

    private void kill_broadcast_receivers()
    {
        broadcaster.unregisterReceiver(bleServiceDiscoveredReceiver);
    }
}
