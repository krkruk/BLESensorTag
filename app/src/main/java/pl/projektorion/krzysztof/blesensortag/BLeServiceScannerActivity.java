package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.FragmentManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Map;
import java.util.UUID;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.GenericGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.notifications.interfaces.NotifyGattProfileInterface;
import pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattModelService;
import pl.projektorion.krzysztof.blesensortag.data.BLeAvailableGattProfiles;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.BLePresentationFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.app.BLeServiceScannerFragment;

public class BLeServiceScannerActivity extends Activity {

    public final static String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.bleservicescanneractivity.extra.BLE_DEVICE";

    private final static String TAG_SERVICE_SCANNER =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.BLeServiceScannerFragment.tag.SERVICE_SCANNER";

    private final static String TAG_BLE_PRESENTATION =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.BLeServiceScannerFragment.tag.BLE_PRESENTATION";

    private final static String EXTRA_SERVICES_DISCOVERED =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.BLeServiceScannerFragment.tag.SERVICES_DISCOVERED";

    private BluetoothDevice bleDevice;
    private BLeServiceScannerFragment serviceScannerFragment;
    private Fragment presentationBleFragment;
    private Context appCtx;

    private TextView labelDeviceName;
    private TextView labelDeviceUuid;

    private MenuItem recordAction;
    private boolean isServiceDiscovered = false;
    private boolean isServiceBound = false;

    private LocalBroadcastManager localBroadcaster;
    private DBServiceBLeService serviceBLe;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isServiceDiscovered = true;
            if( recordAction != null ) recordAction.setEnabled(true);

        }
    };

    private ServiceConnection dbServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceBLe =
                    ((DBServiceBLeService.DBServiceBLeBinder) service).getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_service_scanner);
        appCtx = getApplicationContext();

        retrieve_intent_data();
        load_saved_instance(savedInstanceState);
        negotiate_service_scanner_fragment();
        init_widgets();
        init_widgets_data();
        init_bound_db_service();
        init_broadcast_receivers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ble_service_scanner, menu);
        init_menu(menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_record:
                trigger_db_recording();
                Log.i("Record", "Action pressed");
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_BLE_DEVICE, bleDevice);
        outState.putBoolean(EXTRA_SERVICES_DISCOVERED, isServiceDiscovered);
    }

    @Override
    protected void onDestroy() {
        localBroadcaster.unregisterReceiver(broadcastReceiver);
        if( isServiceBound ) appCtx.unbindService(dbServiceConn);
        super.onDestroy();
    }

    private void trigger_db_recording()
    {
        serviceBLe.setProfiles(filtered_profiles());

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startService(new Intent(appCtx, DBServiceBLeService.class));
                appCtx.unbindService(dbServiceConn);
                isServiceBound = false;
                moveTaskToBack(true);
            }
        }, 500);
    }

    private BLeAvailableGattProfiles filtered_profiles()
    {
        BLeAvailableGattProfiles filteredProfiles = new BLeAvailableGattProfiles();
        final BLeAvailableGattProfiles allProfiles = serviceScannerFragment.getProfiles();

        for( Map.Entry entry : allProfiles.entrySet() )
        {
            final GenericGattProfileInterface profile = (GenericGattProfileInterface)
                    entry.getValue();
            if( profile instanceof NotifyGattProfileInterface )
            {
                if( ((NotifyGattProfileInterface) profile).isNotifying() )
                    filteredProfiles.put( (UUID) entry.getKey(), profile);
            }
        }

        return filteredProfiles;
    }

    private void retrieve_intent_data()
    {
        Intent sentIntent = getIntent();
        this.bleDevice = sentIntent.getParcelableExtra(EXTRA_BLE_DEVICE);
    }

    private void negotiate_service_scanner_fragment()
    {
        FragmentManager fm = getFragmentManager();
        serviceScannerFragment = (BLeServiceScannerFragment)
                fm.findFragmentByTag(TAG_SERVICE_SCANNER);
        if( serviceScannerFragment == null ) {
            FragmentTransaction ft = fm.beginTransaction();
            serviceScannerFragment = BLeServiceScannerFragment.newInstance(bleDevice);
            ft.add(serviceScannerFragment, TAG_SERVICE_SCANNER);
            ft.replace(R.id.ble_service_scanner_fragment_container,
                    serviceScannerFragment);
            ft.commit();
        }

        presentationBleFragment = fm.findFragmentByTag(TAG_BLE_PRESENTATION);
        if( presentationBleFragment == null )
        {
            presentationBleFragment = new BLePresentationFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(presentationBleFragment, TAG_BLE_PRESENTATION);
            ft.replace(R.id.ble_presentation_container, presentationBleFragment);
            ft.commit();
        }
    }

    private void load_saved_instance(Bundle savedInstanceState)
    {
        if( savedInstanceState == null )
            return;

        bleDevice = savedInstanceState.getParcelable(EXTRA_BLE_DEVICE);
        isServiceDiscovered = savedInstanceState.getBoolean(EXTRA_SERVICES_DISCOVERED);
    }

    private void init_widgets()
    {
        labelDeviceName = (TextView) findViewById(R.id.label_current_device_name);
        labelDeviceUuid = (TextView) findViewById(R.id.label_current_device_address);
    }

    private void init_widgets_data()
    {
        if( bleDevice == null )
            return;

        labelDeviceName.setText(bleDevice.getName());
        labelDeviceUuid.setText(bleDevice.getAddress());
    }

    private void init_broadcast_receivers()
    {
        localBroadcaster = LocalBroadcastManager.getInstance(this);
        localBroadcaster.registerReceiver(broadcastReceiver,
                new IntentFilter(BLeGattModelService.ACTION_GATT_MODELS_CREATED));
    }

    private void init_menu(Menu menu)
    {
        recordAction = menu.findItem(R.id.action_record);
        recordAction.setEnabled(isServiceDiscovered);
    }

    private void init_bound_db_service()
    {
        appCtx.bindService(new Intent(appCtx, DBServiceBLeService.class),
                dbServiceConn, Context.BIND_AUTO_CREATE);
    }
}
