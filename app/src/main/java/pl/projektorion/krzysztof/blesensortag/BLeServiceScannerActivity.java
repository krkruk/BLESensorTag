package pl.projektorion.krzysztof.blesensortag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import pl.projektorion.krzysztof.blesensortag.bluetooth.interfaces.BLeGattIO;
import pl.projektorion.krzysztof.blesensortag.bluetooth.service.BLeGattIOService;
import pl.projektorion.krzysztof.blesensortag.fragments.presentation.BLePresentationFragment;
import pl.projektorion.krzysztof.blesensortag.fragments.app.BLeServiceScannerFragment;

public class BLeServiceScannerActivity extends Activity {

    public final static String EXTRA_BLE_DEVICE =
            "pl.projektorion.krzysztof.blesensortag.bleservicescanneractivity.extra.BLE_DEVICE";

    private final static String TAG_SERVICE_SCANNER =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.BLeServiceScannerFragment.tag.SERVICE_SCANNER";

    private final static String TAG_BLE_PRESENTATION =
            "pl.projektorion.krzysztof.blesensortag.fragments.app.BLeServiceScannerFragment.tag.BLE_PRESENTATION";

    private BluetoothDevice bleDevice;
    private Fragment serviceScannerFragment;
    private Fragment presentationBleFragment;

    private TextView labelDeviceName;
    private TextView labelDeviceUuid;

    private MenuItem recordAction;
    private boolean isServiceDiscovered = false;

    private LocalBroadcastManager localBroadcaster;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                isServiceDiscovered = true;
                if( recordAction != null ) recordAction.setEnabled(isServiceDiscovered);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_service_scanner);

        retrieve_intent_data();
        load_saved_instance(savedInstanceState);
        negotiate_service_scanner_fragment();
        init_widgets();
        init_widgets_data();
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
                Log.i("Click", "Record button clicked!");
                break;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_BLE_DEVICE, bleDevice);
    }

    @Override
    protected void onDestroy() {
        localBroadcaster.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void retrieve_intent_data()
    {
        Intent sentIntent = getIntent();
        this.bleDevice = sentIntent.getParcelableExtra(EXTRA_BLE_DEVICE);
    }

    private void negotiate_service_scanner_fragment()
    {
        FragmentManager fm = getFragmentManager();
        serviceScannerFragment = fm.findFragmentByTag(TAG_SERVICE_SCANNER);
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
                new IntentFilter(BLeGattIOService.ACTION_GATT_SERVICES_DISCOVERED));
    }

    private void init_menu(Menu menu)
    {
        recordAction = menu.findItem(R.id.action_record);
        recordAction.setEnabled(isServiceDiscovered);
    }
}
